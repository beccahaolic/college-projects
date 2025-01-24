package com.iotproject.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iotproject.model.Device;

/**
 * Repositório para a entidade Device.
 * Fornece operações de CRUD e permite a definição de consultas personalizadas.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    // Verifica se já existe um dispositivo com o nome
        boolean existsByName(String name);
}