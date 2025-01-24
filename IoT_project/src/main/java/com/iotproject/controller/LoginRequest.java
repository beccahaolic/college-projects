package com.iotproject.controller;

public class LoginRequest {

    private String username;
    private String password;

    /**
     * Construtor padrão da classe LoginRequest.
     * Inicializa os campos de nome de utilizador e palavra-passe com valores nulos.
     */
    public LoginRequest() {
    }

    /**
     * Construtor da classe LoginRequest.
     * Inicializa os campos com os valores fornecidos.
     * 
     * @param username Nome de utilizador.
     * @param password Palavra-passe do utilizador.
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Obtém o nome de utilizador.
     * 
     * @return O nome de utilizador.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Define o nome de utilizador.
     * 
     * @param username O nome de utilizador a ser definido.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtém a palavra-passe.
     * 
     * @return A palavra-passe.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define a palavra-passe.
     * 
     * @param password A palavra-passe a ser definida.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
