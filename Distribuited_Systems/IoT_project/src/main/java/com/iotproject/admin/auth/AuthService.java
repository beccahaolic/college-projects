package com.iotproject.admin.auth;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iotproject.admin.util.HttpClientUtil;

public class AuthService {
    // Variável estática para armazenar o token de autenticação
    public static String token;

    /**
     * Realiza a autenticação do usuário e obtém o token de acesso.
     * 
     * @param scanner O scanner usado para obter as informações de login do usuário.
     * @return true se a autenticação for bem-sucedida, caso contrário, false.
     * @throws Exception caso ocorra algum erro ao enviar a solicitação HTTP.
     */
    public static boolean authenticate(Scanner scanner) throws Exception {
        // Solicita ao usuário o nome de usuário e senha
        System.out.println("Digite o nome de usuário:");
        String username = scanner.nextLine();
        System.out.println("Digite a senha:");
        String password = scanner.nextLine();

        // Cria o corpo da requisição com os dados de login
        String jsonBody = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

        // Constrói a requisição HTTP para autenticação
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/auth/signin"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Envia a requisição e obtém a resposta
        HttpResponse<String> response = HttpClientUtil.sendRequest(request);

        // Verifica se a resposta foi bem-sucedida (código 200)
        if (response.statusCode() == 200) {
            // Parse do corpo da resposta para obter o token de acesso
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            token = jsonObject.get("accessToken").getAsString();  // Armazena o token na variável estática
            System.out.println("Autenticação bem-sucedida! Token obtido.");
            return true;
        } else {
            // Caso a autenticação falhe, exibe a mensagem de erro
            System.out.println("Erro na autenticação: " + response.body());
            return false;
        }
    }
}
