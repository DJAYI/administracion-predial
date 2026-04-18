package com.agmdesarrollos.geopix.security.http.services;

import com.agmdesarrollos.geopix.security.http.dto.*;
import com.agmdesarrollos.geopix.security.logic.AuthenticationService;
import com.agmdesarrollos.geopix.security.persistance.entities.Role;
import com.agmdesarrollos.geopix.security.persistance.entities.User;
import com.agmdesarrollos.geopix.security.persistance.repository.JpaRoleRepository;
import com.agmdesarrollos.geopix.security.persistance.repository.JpaUserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthHttpService {

    private final AuthenticationService authenticationService;
    private final JpaUserRepository userRepository;
    private final JpaRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO request, HttpServletResponse response) {
        // Find user to get the real username if email was provided
        User user = userRepository.findByUsername(request.getIdentifier())
                .orElseGet(() -> userRepository.findByEmail(request.getIdentifier())
                        .orElseThrow(() -> new RuntimeException("User not found")));

        authenticationService.authenticate(user.getUsername(), request.getPassword(), response);

        return LoginResponseDTO.builder()
                .id(user.getId())
                .identifier(user.getUsername())
                .roles(user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();
    }

    public MeResponseDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("Not authenticated");
        }
        User user = (User) authentication.getPrincipal();
        
        return MeResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        String roleName = request.getRole() != null ? request.getRole() : "USER";
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        return RegisterResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().getName())
                .build();
    }
}
