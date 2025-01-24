package com.iotproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Classe que representa um dispositivo no sistema.
* Mapeada para a tabela "Devices" no banco de dados.
*/
@Entity
@Table(name = "Devices")
public class Device {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String room;
    private String service;
    private int floor;
    private String building;
    
    /**
    * Construtor sem argumentos.
    * Necessário para o JPA.
    */
    public Device() {
    }

    /**
    * Construtor com todos os argumentos.
    * 
    * @param id Identificador único do dispositivo.
    * @param name Nome do dispositivo.
    * @param type Tipo do dispositivo.
    * @param room Sala onde o dispositivo está localizado.
    * @param service Serviço associado ao dispositivo.
    * @param floor Andar onde o dispositivo está localizado.
    * @param building Prédio onde o dispositivo está localizado.
    */
    public Device(Long id, String name, String type, String room, String service, int floor, String building) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.room = room;
        this.service = service;
        this.floor = floor;
        this.building = building;
    }

    // Getters e Setters para os atributos da classe
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    /**
    * Método toString para representar o dispositivo como uma string.
    * 
    * @return String representando o dispositivo.
    */
    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", room='" + room + '\'' +
                ", service='" + service + '\'' +
                ", floor=" + floor + // Ajustado
                ", building='" + building + '\'' +
                '}';
    }
}
