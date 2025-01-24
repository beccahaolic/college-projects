package com.iotproject.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iotproject.model.Metrics;

@Repository
public interface MetricsRepository extends CrudRepository<Metrics, Long> {

    // Consulta para obter as médias por sala
    @Query("SELECT m.device.room AS room, AVG(m.temperature) AS avgTemp, AVG(m.humidity) AS avgHumidity " +
           "FROM Metrics m " +
           "WHERE m.timestamp BETWEEN :start AND :end " +
           "GROUP BY m.device.room")
    List<Object[]> findAverageMetricsByRoom(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Consulta para obter as médias por serviço
    @Query("SELECT m.device.service AS service, AVG(m.temperature) AS avgTemp, AVG(m.humidity) AS avgHumidity " +
           "FROM Metrics m " +
           "WHERE m.timestamp BETWEEN :start AND :end " +
           "GROUP BY m.device.service")
    List<Object[]> findAverageMetricsByService(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Consulta para obter as médias por piso
    @Query("SELECT m.device.floor AS floor, AVG(m.temperature) AS avgTemp, AVG(m.humidity) AS avgHumidity " +
           "FROM Metrics m " +
           "WHERE m.timestamp BETWEEN :start AND :end " +
           "GROUP BY m.device.floor")
    List<Object[]> findAverageMetricsByFloor(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Consulta para obter as médias por edifício
    @Query("SELECT m.device.building AS building, AVG(m.temperature) AS avgTemp, AVG(m.humidity) AS avgHumidity " +
           "FROM Metrics m " +
           "WHERE m.timestamp BETWEEN :start AND :end " +
           "GROUP BY m.device.building")
    List<Object[]> findAverageMetricsByBuilding(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
