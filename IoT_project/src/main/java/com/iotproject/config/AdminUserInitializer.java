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

/**
* Classe de configuração para inicializar o usuário administrador.
* Esta classe garante que um usuário administrador seja criado com as propriedades especificadas.
*/
@Configuration
public class AdminUserInitializer {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserInitializer.class);

    // Injeção das propriedades do administrador a partir da configuração da aplicação
    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.role:ROLE_ADMIN}")
    private String adminRole;

    /**
    * Bean que é executado na inicialização da aplicação para criar o usuário administrador, se ele não existir.
    *
    * @param userRepository Repositório para entidades de usuário.
    * @param roleRepository Repositório para entidades de papel.
    * @param passwordEncoder Codificador para hashing de senhas.
    * @return CommandLineRunner para executar a lógica de inicialização.
    */
    @Bean
    public CommandLineRunner createAdminUser(UserRepository userRepository,
                                             RoleRepository roleRepository,
                                             PasswordEncoder passwordEncoder) {
        return args -> {
            // Verifica se a configuração do administrador é válida
            if (isConfigInvalid()) {
                logger.error("Configuração inválida para o administrador. Verifique as propriedades 'admin.*'.");
                return;
            }

            ERole adminRoleEnum;
            try {
                // Converte a string do papel do administrador para um enum ERole
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

    /**
    * Valida as propriedades de configuração do administrador.
    *
    * @return true se alguma das propriedades do administrador for inválida, false caso contrário.
    */

    private boolean isConfigInvalid() {
        return adminUsername == null || adminUsername.isBlank()
            || adminPassword == null || adminPassword.isBlank()
            || adminEmail == null || adminEmail.isBlank();
    }
}
