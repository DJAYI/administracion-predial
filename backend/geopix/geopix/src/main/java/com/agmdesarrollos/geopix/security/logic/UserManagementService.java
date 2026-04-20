package com.agmdesarrollos.geopix.security.logic;

import com.agmdesarrollos.geopix.security.persistance.entities.Role;
import com.agmdesarrollos.geopix.security.persistance.entities.User;
import com.agmdesarrollos.geopix.security.persistance.repository.JpaRoleRepository;
import com.agmdesarrollos.geopix.security.persistance.repository.JpaUserRepository;
import com.agmdesarrollos.geopix.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final JpaUserRepository userRepository;
    private final JpaRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public User create(User user, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        user.setEnabled(true);
        user.setDeleted(false);
        
        return userRepository.save(user);
    }

    @Transactional
    public User update(UUID id, User userDetails, String roleName) {
        User user = findById(id);
        
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        
        if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        if (roleName != null) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
            user.setRole(role);
        }
        
        user.setEnabled(userDetails.isEnabled());
        
        return userRepository.save(user);
    }

    @Transactional
    public void delete(UUID id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
