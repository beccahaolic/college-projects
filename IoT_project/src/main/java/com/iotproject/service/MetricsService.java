package com.iotproject.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iotproject.repository.MetricsRepository;

@Service
public class MetricsService {

    @Autowired
    private MetricsRepository metricsRepository;

    public List<Object[]> getAverageMetricsByRoom(LocalDateTime start, LocalDateTime end) {
        return metricsRepository.findAverageMetricsByRoom(start, end);
    }

    public List<Object[]> getAverageMetricsByService(LocalDateTime start, LocalDateTime end) {
        return metricsRepository.findAverageMetricsByService(start, end);
    }

    public List<Object[]> getAverageMetricsByFloor(LocalDateTime start, LocalDateTime end) {
        return metricsRepository.findAverageMetricsByFloor(start, end);
    }

    public List<Object[]> getAverageMetricsByBuilding(LocalDateTime start, LocalDateTime end) {
        return metricsRepository.findAverageMetricsByBuilding(start, end);
    }
}
