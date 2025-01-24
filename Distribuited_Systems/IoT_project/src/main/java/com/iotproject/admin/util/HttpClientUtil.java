package com.iotproject.admin.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.iotproject.admin.auth.AuthService;

public class HttpClientUtil {
    private static final HttpClient client = HttpClient.newHttpClient();  // Instância única de HttpClient

    /**
     * Constrói uma requisição HTTP GET com autenticação.
     *
     * @param url A URL para a qual a requisição será enviada.
     * @return Um objeto HttpRequest configurado para um GET.
     */
    public static HttpRequest buildGetRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))  // Define a URL de destino
                .header("Authorization", "Bearer " + AuthService.token)  // Autenticação com token
                .GET()  // Define o método HTTP GET
                .build();
    }

    /**
     * Constrói uma requisição HTTP POST com corpo e autenticação.
     *
     * @param url  A URL para a qual a requisição será enviada.
     * @param body O corpo da requisição, geralmente no formato JSON.
     * @return Um objeto HttpRequest configurado para um POST.
     */
    public static HttpRequest buildPostRequest(String url, String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))  // Define a URL de destino
                .header("Content-Type", "application/json")  // Define o tipo de conteúdo como JSON
                .header("Authorization", "Bearer " + AuthService.token)  // Autenticação com token
                .POST(HttpRequest.BodyPublishers.ofString(body))  // Define o método HTTP POST com o corpo da requisição
                .build();
    }

    /**
     * Constrói uma requisição HTTP PUT com corpo e autenticação.
     *
     * @param url  A URL para a qual a requisição será enviada.
     * @param body O corpo da requisição, geralmente no formato JSON.
     * @return Um objeto HttpRequest configurado para um PUT.
     */
    public static HttpRequest buildPutRequest(String url, String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))  // Define a URL de destino
                .header("Content-Type", "application/json")  // Define o tipo de conteúdo como JSON
                .header("Authorization", "Bearer " + AuthService.token)  // Autenticação com token
                .PUT(HttpRequest.BodyPublishers.ofString(body))  // Define o método HTTP PUT com o corpo da requisição
                .build();
    }

    /**
     * Constrói uma requisição HTTP DELETE com autenticação.
     *
     * @param url A URL para a qual a requisição será enviada.
     * @return Um objeto HttpRequest configurado para um DELETE.
     */
    public static HttpRequest buildDeleteRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))  // Define a URL de destino
                .header("Authorization", "Bearer " + AuthService.token)  // Autenticação com token
                .DELETE()  // Define o método HTTP DELETE
                .build();
    }

    /**
     * Envia uma requisição HTTP e retorna a resposta.
     *
     * @param request A requisição HTTP a ser enviada.
     * @return A resposta HTTP recebida, contendo o corpo como String.
     * @throws Exception Se ocorrer algum erro durante o envio da requisição.
     */
    public static HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        return client.send(request, HttpResponse.BodyHandlers.ofString());  // Envia a requisição e retorna a resposta como String
    }
}
