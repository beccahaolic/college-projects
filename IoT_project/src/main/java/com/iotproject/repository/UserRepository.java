package com.iotproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iotproject.model.User;

/**
 * Repositório para a entidade User.
 * Fornece operações de CRUD e permite a definição de consultas personalizadas.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Encontra um usuário pelo nome de usuário.
     * 
     * @param username Nome de usuário.
     * @return Um Optional contendo o usuário, se encontrado.
    */
    Optional<User> findByUsername(String username);

    /**
     * Verifica se um usuário com o nome de usuário especificado existe.
     * 
     * @param username Nome de usuário.
     * @return true se o usuário existir, false caso contrário.
    */
    Boolean existsByUsername(String username);

    /**
     * Verifica se um usuário com o email especificado existe.
     * 
     * @param email Email do usuário.
     * @return true se o usuário existir, false caso contrário.
    */
    Boolean existsByEmail(String email);
} 