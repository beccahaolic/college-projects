package com.iotproject.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
* Classe que representa um usuário no sistema.
* Mapeada para a tabela "users" no banco de dados.
* Possui restrições de unicidade para os campos "username" e "email".
*/
@Entity
@Table(name = "users", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // Identificador único do usuário

  @NotBlank
  @Size(max = 20)
  private String username; // Nome de usuário

  @NotBlank
  @Size(max = 50)
  @Email
  private String email; // Endereço de e-mail

  @NotBlank
  @Size(max = 120)
  private String password; // Senha do usuário

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>(); // Conjunto de papéis (roles) associados ao usuário

  /**
  * Construtor sem argumentos.
  * Necessário para o JPA.
  */
  public User() {
  }

  /**
  * Construtor com todos os argumentos.
  * 
  * @param username Nome de usuário.
  * @param email Email do usuário.
  * @param password Senha do usuário.
  */
  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  // Getters e Setters para os atributos da classe

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}