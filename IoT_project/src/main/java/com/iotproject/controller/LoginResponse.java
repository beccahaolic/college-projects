package com.iotproject.controller;

public class LoginResponse {

    private String token;

    /**
     * Construtor da classe LoginResponse.
     * Inicializa o campo 'token' com o valor fornecido.
     * 
     * @param token O token JWT gerado após a autenticação.
     */
    public LoginResponse(String token) {
        this.token = token;
    }

    /**
     * Obtém o token.
     * 
     * @return O token JWT.
     */
    public String getToken() {
        return token;
    }

    /**
     * Define o token.
     * 
     * @param token O token JWT a ser definido.
     */
    public void setToken(String token) {
        this.token = token;
    }
}
