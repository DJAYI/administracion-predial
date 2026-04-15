package com.agmdesarrollos.geopix.configuration;

import com.agmdesarrollos.geopix.security.jpa.JpaRoleRepository;
import com.agmdesarrollos.geopix.security.jpa.JpaUserRepository;
import com.agmdesarrollos.geopix.security.jpa.RoleEntity;
import com.agmdesarrollos.geopix.security.jpa.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private static final String[] DEFAULT_ROLES = {
            "ADMIN",
            "CONTADOR",
            "EJECUTOR_INTEGRAL",
            "LECTOR"
    };

    @Bean
    public CommandLineRunner initRolesAndAdmin(JpaRoleRepository roleRepository,
                                               JpaUserRepository userRepository,
                                               PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed roles
            for (String roleName : DEFAULT_ROLES) {
                if (!roleRepository.existsByName(roleName)) {
                    roleRepository.save(new RoleEntity(roleName));
                    log.info("Role created: {}", roleName);
                }
            }

            // Seed default admin user
            if (!userRepository.existsByUsername("admin")) {
                RoleEntity adminRole = roleRepository.findByName("ADMIN")
                        .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

                UserEntity admin = new UserEntity();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@geopix.com");
                admin.setEnabled(true);
                admin.setRoles(Set.of(adminRole));

                userRepository.save(admin);
                log.info("Default admin user created (username: admin)");
            }
        };
    }
}
