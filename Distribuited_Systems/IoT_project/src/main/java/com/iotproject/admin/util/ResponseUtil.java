package com.iotproject.admin.util;

import java.net.http.HttpResponse;

public class ResponseUtil {
    /**
     * Manipula a resposta da requisição HTTP.
     * Se o código de status não for 200 ou 201, imprime um erro.
     *
     * @param response A resposta HTTP recebida da requisição.
     */
    public static void handleResponse(HttpResponse<String> response) {
        // Verifica se o código de status não é 200 (OK) ou 201 (Criado)
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            // Caso o código de status seja diferente de 200 ou 201, imprime o erro
            System.out.println("Erro: " + response.statusCode());
            System.out.println(response.body());  // Exibe o corpo da resposta (geralmente uma mensagem de erro)
        }
        // Se a resposta for bem-sucedida (status 200 ou 201), nada é impresso
        // Isso é intencional para evitar sobrecarga de mensagens em caso de sucesso.
    }
}
