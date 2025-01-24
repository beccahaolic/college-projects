package com.iotproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Classe que representa um papel (role) no sistema.
* Mapeada para a tabela "roles" no banco de dados.
*/
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Identificador único do papel

    @Enumerated(EnumType.STRING)
    private ERole name; // Nome do papel, representado pela enumeração ERole

    /**
    * Construtor sem argumentos.
    * Necessário para o JPA.
    */
    public Role() {
    }

    /**
    * Construtor com todos os argumentos.
    * 
    * @param id Identificador único do papel.
    * @param name Nome do papel, representado pela enumeração ERole.
    */
    public Role(Long id, ERole name) {
        this.id = id;
        this.name = name;
    }

    // Getters e Setters para os atributos da classe
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

    /**
    * Método toString para representar o papel como uma string.
    * 
    * @return String representando o papel.
    */
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}