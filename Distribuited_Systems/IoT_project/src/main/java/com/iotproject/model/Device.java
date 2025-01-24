package com.iotproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "devices")
public class Device {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String room;
    private String service;
    private int floor; // Alterado para int
    private String building;

    // Construtor sem argumentos
    public Device() {
    }

    // Construtor com todos os argumentos
    public Device(Long id, String name, String type, String room, String service, int floor, String building) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.room = room;
        this.service = service;
        this.floor = floor;
        this.building = building;
    }

    // Getters e Setters
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

    public Integer getFloor() { // Ajustado
        return floor;
    }

    public void setFloor(int floor) { // Ajustado
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

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
