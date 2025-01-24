package com.iotproject.payload.response;

import java.util.List;

/**
* Classe que representa a resposta contendo o token JWT.
* Inclui informações do usuário autenticado e seus papéis (roles).
*/
public class JwtResponse { 
  private String token; // Token JWT
  private String type = "Bearer"; // Tipo do token (Bearer)
  private Long id; // Identificador único do usuário
  private String username; // Nome de usuário
  private String email; // Email do usuário
  private final List<String> roles; // Lista de papéis (roles) do usuário

  /**
  * Construtor com todos os argumentos.
  * 
  * @param accessToken Token de acesso JWT.
  * @param id Identificador único do usuário.
  * @param username Nome de usuário.
  * @param email Email do usuário.
  * @param roles Lista de papéis (roles) do usuário.
  */
  public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
  } 

  // Getters e Setters para os atributos da classe
  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return roles;
  }
}