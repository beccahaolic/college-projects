package com.iotproject.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
* Classe que representa as métricas de um dispositivo no sistema.
* Mapeada para a tabela "Metrics" no banco de dados.
*/
@Entity
@Table(name = "Metrics")
public class Metrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único da métrica
    
    private double temperature; // Temperatura registrada pelo dispositivo
    private double humidity; // Umidade registrada pelo dispositivo

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp; // Data e hora em que a métrica foi registrada

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device; // Dispositivo ao qual a métrica está associada

    /**
    * Construtor sem argumentos.
    * Necessário para o JPA.
    */
    public Metrics() {
    }

    /**
    * Construtor com todos os argumentos.
    * 
    * @param temperature Temperatura registrada pelo dispositivo.
    * @param humidity Umidade registrada pelo dispositivo.
    * @param timestamp Data e hora em que a métrica foi registrada.
    * @param device Dispositivo ao qual a métrica está associada.
    */
    public Metrics(double temperature, double humidity, LocalDateTime timestamp, Device device) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
        this.device = device;
    }

    // Getters e Setters para os atributos da classe
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    /**
    * Método toString para representar a métrica como uma string.
    * 
    * @return String representando a métrica.
    */
    @Override
    public String toString() {
        return "Metrics{" +
                "id=" + id +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", timestamp=" + timestamp +
                ", device=" + (device != null ? device.getId() : "null") +
                '}';
    }
}
