package com.agmdesarrollos.geopix.config;

import com.agmdesarrollos.geopix.features.utils.logic.GetDepartmentsAndMunicipalities;
import com.agmdesarrollos.geopix.features.utils.persistance.entities.Departamento;
import com.agmdesarrollos.geopix.features.utils.persistance.repositories.DepartamentoRepository;
import com.agmdesarrollos.geopix.security.persistance.entities.Role;
import com.agmdesarrollos.geopix.security.persistance.entities.User;
import com.agmdesarrollos.geopix.security.persistance.repository.JpaRoleRepository;
import com.agmdesarrollos.geopix.security.persistance.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final GetDepartmentsAndMunicipalities getDepartmentsAndMunicipalities;
    private final DepartamentoRepository departamentoRepository;
    private final JpaRoleRepository roleRepository;
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    public CommandLineRunner initData() {
        return args -> {
            // 1. Initialize Departments and Municipalities
            if (departamentoRepository.count() == 0) {
                List<Departamento> departamentos = getDepartmentsAndMunicipalities.execute();
                departamentoRepository.saveAll(departamentos);
                System.out.println("Loaded " + departamentos.size() + " departments and municipalities.");
            } else {
                System.out.println("Departments and municipalities already exist, skipping data initialization.");
            }

            // 2. Initialize Roles
            List<String> defaultRoles = Arrays.asList("ADMIN", "CONTADOR", "EJECUTOR_INTEGRAL");
            for (String roleName : defaultRoles) {
                if (roleRepository.findByName(roleName).isEmpty()) {
                    Role role = new Role(null, roleName);
                    roleRepository.save(role);
                    System.out.println("Created role: " + roleName);
                } else {
                    System.out.println("Role " + roleName + " already exists.");
                }
            }

            // 3. Create Default ADMIN User
            String adminUsername = "admin";
            String adminEmail = "admin@geopix.com";
            String adminPassword = "password"; // You should use a strong password in production

            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                Optional<Role> adminRoleOptional = roleRepository.findByName("ADMIN");
                if (adminRoleOptional.isPresent()) {
                    User adminUser = User.builder()
                            .username(adminUsername)
                            .email(adminEmail)
                            .password(passwordEncoder.encode(adminPassword))
                            .role(adminRoleOptional.get())
                            .build();
                    userRepository.save(adminUser);
                    System.out.println("Created default ADMIN user: " + adminUsername);
                } else {
                    System.err.println("ADMIN role not found, cannot create default ADMIN user.");
                }
            } else {
                System.out.println("Default ADMIN user " + adminUsername + " already exists, skipping creation.");
            }
        };
    }
}
