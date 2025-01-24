package com.iotproject.payload.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Classe que representa a requisição de cadastro.
 * Contém os campos necessários para registrar um novo usuário.
 */
public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String username; // Nome de usuário para cadastro

  @NotBlank
  @Size(max = 50)
  @Email
  private String email; // Email do usuário para cadastro

  private Set<String> role; // Conjunto de papéis (roles) associados ao usuário

  @NotBlank
  @Size(min = 6, max = 40)
  private String password; // Senha do usuário para cadastro

  // Getters e Setters para os atributos da classe
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

  public Set<String> getRole() {
    return this.role;
  }

  public void setRole(Set<String> role) {
    this.role = role;
  }
}