package com.iotproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iotproject.model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    // Verifica se já existe um dispositivo com o nome
    boolean existsByName(String name);

    // Query para excluir métricas associadas antes de excluir o dispositivo
    @Modifying  // Indica que é uma operação de modificação no banco de dados
    @Transactional  // Garante que a operação seja executada dentro de uma transação
    @Query("DELETE FROM Metrics m WHERE m.device.id = :deviceId")
    void deleteMetricsByDeviceId(@Param("deviceId") Long deviceId);
}
