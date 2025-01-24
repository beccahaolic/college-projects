package com.iotproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

import com.iotproject.model.ERole;
import com.iotproject.model.Role;

/**
 * Repositório para a entidade Role.
 * Fornece operações de CRUD e permite a definição de consultas personalizadas.
*/
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Encontra um papel (role) pelo nome.
     * 
     * @param name Nome do papel, representado pela enumeração ERole.
     * @return Um Optional contendo o papel, se encontrado.
    */
    Optional<Role> findByName(ERole name);
}