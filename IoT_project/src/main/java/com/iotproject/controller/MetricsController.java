package com.iotproject.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iotproject.service.MetricsService;

@RestController
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    /**
     * Endpoint para consultar métricas agregadas.
     *
     * @param type      O tipo de agregação (room, service, floor, building).
     * @param startTime O horário de início no formato yyyy-MM-dd HH:mm:ss (opcional).
     * @param endTime   O horário de fim no formato yyyy-MM-dd HH:mm:ss (opcional).
     * @return Uma lista de métricas agregadas.
     */
    @GetMapping("/metrics/aggregated")
    public List<Object[]> getAggregatedMetrics(
            @RequestParam String type,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        try {
            // Usar DateTimeFormatter para definir o formato esperado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


            // Se startTime ou endTime estiverem nulos, usar o formato default
            LocalDateTime start = startTime != null ? parseDate(startTime, formatter) : LocalDateTime.now().minusHours(24);
            LocalDateTime end = endTime != null ? parseDate(endTime, formatter) : LocalDateTime.now();

            // Obter as métricas agregadas com base no tipo
            List<Object[]> result;
            switch (type.toLowerCase()) {
                case "room":
                    result = metricsService.getAverageMetricsByRoom(start, end);
                    break;
                case "service":
                    result = metricsService.getAverageMetricsByService(start, end);
                    break;
                case "floor":
                    result = metricsService.getAverageMetricsByFloor(start, end);
                    break;
                case "building":
                    result = metricsService.getAverageMetricsByBuilding(start, end);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de agregação inválido: " + type);
            }

            return result;
        } catch (Exception e) {
            // Tratar exceções e retornar uma resposta apropriada
            throw new RuntimeException("Erro ao obter métricas agregadas", e);
        }
    }

    /**
     * Analisa uma string de data para um objeto LocalDateTime.
     *
     * @param dateStr   A string de data a ser analisada.
     * @param formatter O formatador de data a ser usado.
     * @return O objeto LocalDateTime correspondente.
     */
    private LocalDateTime parseDate(String dateStr, DateTimeFormatter formatter) {
        return LocalDateTime.parse(dateStr, formatter);
    }
}