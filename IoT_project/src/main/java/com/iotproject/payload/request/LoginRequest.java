package com.iotproject.payload.request;

import jakarta.validation.constraints.NotBlank;

/**
* Classe que representa a requisição de login.
* Contém os campos necessários para autenticação do usuário.
*/
public class LoginRequest {
    @NotBlank
    private String username; // Nome de usuário para login

    @NotBlank
    private String password; // Nome de usuário para login

    // Getters e Setters para os atributos da classe

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() { 
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}