package com.iotproject.admin.metric;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.iotproject.admin.util.HttpClientUtil;
import com.iotproject.admin.util.TableDisplayUtil;

public class MetricService {
    private static final String BASE_URL = "http://localhost:8080";  // URL base da API
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  // Formato de data para as métricas

    /**
     * Método para consultar métricas agregadas.
     * 
     * @param scanner O scanner utilizado para capturar dados do utilizador.
     * @throws Exception Se ocorrer um erro ao enviar a solicitação HTTP.
     */
    public static void queryAggregatedMetrics(Scanner scanner) throws Exception {
        // Tipos válidos para agregação
        List<String> validTypes = Arrays.asList("room", "service", "floor", "building");
        System.out.println("Tipo de agregação (room, service, floor, building):");
        String type = scanner.nextLine();

        // Verificação se o tipo fornecido é válido
        if (!validTypes.contains(type)) {
            System.out.println("Tipo de agregação inválido. Tente novamente.");
            return;
        }

        // Obter os parâmetros de início e fim para a consulta, com valores padrão de 24 horas atrás e agora
        String start = getDateFromInput(scanner, "Início (opcional, formato yyyy-MM-dd HH:mm:ss):", -24);
        String end = getDateFromInput(scanner, "Fim (opcional, formato yyyy-MM-dd HH:mm:ss):", 0);

        System.out.println("Start: " + start);
        System.out.println("End: " + end);

        // Codificar os parâmetros start e end para garantir que o URL seja válido
        String encodedStart = encodeParam(start);
        String encodedEnd = encodeParam(end);

        // Construir a URL da API com os parâmetros codificados
        String url = String.format("%s/metrics/aggregated?type=%s&startTime=%s&endTime=%s", 
                                   BASE_URL, type, encodedStart, encodedEnd);

        // Criar URI com a URL gerada
        URI uri = URI.create(url);

        // Construir o request HTTP com o método GET
        HttpRequest request = HttpClientUtil.buildGetRequest(uri.toString());
        HttpResponse<String> response = HttpClientUtil.sendRequest(request);
        

        // Fazer o parse da resposta como um JsonArray e exibi-la em forma de tabela
        JsonArray metrics = JsonParser.parseString(response.body()).getAsJsonArray();
        TableDisplayUtil.displayMetricsAsTable(metrics);
    }

    /**
     * Método auxiliar para obter e formatar a data a partir da entrada do utilizador.
     * 
     * @param scanner O scanner utilizado para capturar dados do utilizador.
     * @param prompt A mensagem que será mostrada ao utilizador.
     * @param hourOffset O deslocamento em horas para a data (para definir um valor padrão).
     * @return A data formatada como uma string no formato yyyy-MM-dd HH:mm:ss.
     */
    private static String getDateFromInput(Scanner scanner, String prompt, int hourOffset) {
        System.out.println(prompt);
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            // Se o utilizador não fornecer uma data, usa a data com o deslocamento de horas
            return LocalDateTime.now().plusHours(hourOffset).format(DATE_TIME_FORMATTER);
        }

        return input;  // Retorna o valor fornecido pelo utilizador
    }

    /**
     * Método auxiliar para codificar parâmetros da URL de forma segura.
     * 
     * @param param O parâmetro a ser codificado.
     * @return O parâmetro codificado.
     */
    private static String encodeParam(String param) {
        return URLEncoder.encode(param, StandardCharsets.UTF_8);
    }
}
