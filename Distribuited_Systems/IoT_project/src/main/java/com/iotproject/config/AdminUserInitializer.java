package com.iotproject.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.iotproject.model.ERole;
import com.iotproject.model.Role;
import com.iotproject.model.User;
import com.iotproject.repository.RoleRepository;
import com.iotproject.repository.UserRepository;

@Configuration
public class AdminUserInitializer {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserInitializer.class);

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.role:ROLE_ADMIN}")
    private String adminRole;

    @Bean
    public CommandLineRunner createAdminUser(UserRepository userRepository,
                                             RoleRepository roleRepository,
                                             PasswordEncoder passwordEncoder) {
        return args -> {
            if (isConfigInvalid()) {
                logger.error("Configuração inválida para o administrador. Verifique as propriedades 'admin.*'.");
                return;
            }

            ERole adminRoleEnum;
            try {
                adminRoleEnum = ERole.valueOf(adminRole);
            } catch (IllegalArgumentException e) {
                logger.error("O valor do papel especificado em 'admin.role' não é válido: {}", adminRole, e);
                return;
            }

            // Verificar e criar o papel ADMIN, se necessário
            Role adminRoleEntity = roleRepository.findByName(adminRoleEnum)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(adminRoleEnum);
                        roleRepository.save(role);
                        logger.info("Papel criado: {}", adminRoleEnum);
                        return role;
                    });

            // Criar usuário administrador, se necessário
            userRepository.findByUsername(adminUsername).ifPresentOrElse(
                user -> logger.info("Usuário administrador já existe: {}", adminUsername),
                () -> {
                    User admin = new User();
                    admin.setUsername(adminUsername);
                    admin.setPassword(passwordEncoder.encode(adminPassword));
                    admin.setEmail(adminEmail);
                    admin.getRoles().add(adminRoleEntity);

                    try {
                        userRepository.save(admin);
                        logger.info("Usuário administrador criado: {}", adminUsername);
                    } catch (Exception e) {
                        logger.error("Erro ao salvar o usuário administrador: {}", adminUsername, e);
                    }
                }
            );
        };
    }

    private boolean isConfigInvalid() {
        return adminUsername == null || adminUsername.isBlank()
            || adminPassword == null || adminPassword.isBlank()
            || adminEmail == null || adminEmail.isBlank();
    }
}
