package com.iotproject.admin.device;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iotproject.admin.auth.AuthService;
import com.iotproject.admin.util.HttpClientUtil;
import com.iotproject.admin.util.ResponseUtil;
import com.iotproject.admin.util.TableDisplayUtil;

public class DeviceService {

    /**
     * Método para listar dispositivos (sem paginação).
     * 
     * @param scanner O scanner usado para interagir com o utilizador.
     * @param baseUrl A URL base da API.
     * @throws Exception Se ocorrer um erro ao enviar a solicitação HTTP.
     */
    public static void listDevices(Scanner scanner, String baseUrl) throws Exception {
        String url = baseUrl + "/devices";  // Usando baseUrl aqui
        HttpRequest request = HttpClientUtil.buildGetRequest(url);
        HttpResponse<String> response = HttpClientUtil.sendRequest(request);
        ResponseUtil.handleResponse(response);

        // Parse da resposta e exibição como tabela
        JsonArray devices = JsonParser.parseString(response.body()).getAsJsonArray();
        TableDisplayUtil.displayDevicesAsTable(devices);
    }

    /**
     * Método para criar um novo dispositivo.
     * 
     * @param scanner O scanner usado para interagir com o utilizador.
     * @param baseUrl A URL base da API.
     * @throws Exception Se ocorrer um erro ao enviar a solicitação HTTP.
     */
    public static void createDevice(Scanner scanner, String baseUrl) throws Exception {
        String jsonBody = collectDeviceData(scanner);  // Coleta os dados do dispositivo
        String url = baseUrl + "/devices";  // Usando baseUrl aqui
        HttpRequest request = HttpClientUtil.buildPostRequest(url, jsonBody);
        HttpResponse<String> response = HttpClientUtil.sendRequest(request);
        ResponseUtil.handleResponse(response);
    }

    /**
     * Método para atualizar um dispositivo existente.
     * 
     * @param scanner O scanner usado para interagir com o utilizador.
     * @param baseUrl A URL base da API.
     * @throws Exception Se ocorrer um erro ao enviar a solicitação HTTP.
     */
    public static void updateDevice(Scanner scanner, String baseUrl) throws Exception {
        System.out.println("Digite o ID do dispositivo a ser atualizado:");
        long id = scanner.nextLong();
        scanner.nextLine();  // Consumir o newline

        // Obter o dispositivo atual
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + "/devices/" + id))  // Usando baseUrl aqui
                .header("Authorization", "Bearer " + AuthService.token)
                .GET()
                .build();
        HttpResponse<String> getResponse = HttpClientUtil.sendRequest(getRequest);
        JsonObject currentDevice = JsonParser.parseString(getResponse.body()).getAsJsonObject();

        // Solicitar as atualizações para cada campo, deixando vazio para manter o valor atual
        System.out.println("Nome do dispositivo (deixe vazio para manter o atual):");
        String name = scanner.nextLine();
        System.out.println("Tipo (deixe vazio para manter o atual):");
        String type = scanner.nextLine();
        System.out.println("Sala (deixe vazio para manter o atual):");
        String room = scanner.nextLine();
        System.out.println("Serviço (deixe vazio para manter o atual):");
        String service = scanner.nextLine();
        System.out.println("Piso (deixe vazio para manter o atual):");
        String floor = scanner.nextLine();
        System.out.println("Edifício (deixe vazio para manter o atual):");
        String building = scanner.nextLine();

        // Construção do JSON para atualização, com valores atuais ou novos valores
        String jsonBody = String.format(
                "{\"name\":\"%s\", \"type\":\"%s\", \"room\":\"%s\", \"service\":\"%s\", \"floor\":%s, \"building\":\"%s\"}",
                name.isEmpty() ? currentDevice.get("name").getAsString() : name,
                type.isEmpty() ? currentDevice.get("type").getAsString() : type,
                room.isEmpty() ? currentDevice.get("room").getAsString() : room,
                service.isEmpty() ? currentDevice.get("service").getAsString() : service,
                floor.isEmpty() ? currentDevice.get("floor").getAsString() : floor,
                building.isEmpty() ? currentDevice.get("building").getAsString() : building);

        // Enviar a requisição PUT para atualização
        String url = baseUrl + "/devices/" + id;  // Usando baseUrl aqui
        HttpRequest request = HttpClientUtil.buildPutRequest(url, jsonBody);
        HttpResponse<String> response = HttpClientUtil.sendRequest(request);
        ResponseUtil.handleResponse(response);
    }

    /**
     * Método para deletar um dispositivo.
     * 
     * @param scanner O scanner usado para interagir com o utilizador.
     * @param baseUrl A URL base da API.
     * @throws Exception Se ocorrer um erro ao enviar a solicitação HTTP.
     */
    public static void deleteDevice(Scanner scanner, String baseUrl) throws Exception {
        System.out.println("Digite o ID do dispositivo a ser removido:");
        long id = scanner.nextLong();
        scanner.nextLine();  // Consumir o newline

        String url = baseUrl + "/devices/" + id;  // Usando baseUrl aqui
        HttpRequest request = HttpClientUtil.buildDeleteRequest(url);
        HttpResponse<String> response = HttpClientUtil.sendRequest(request);
        ResponseUtil.handleResponse(response);
    }

    /**
     * Método para fazer o dump de dispositivos de teste com número personalizado.
     * 
     * @param baseUrl A URL base da API.
     * @param scanner O scanner usado para interagir com o utilizador.
     * @throws Exception Se ocorrer um erro ao enviar a solicitação HTTP.
     */
    public static void dumpDevices(String baseUrl, Scanner scanner) throws Exception {
        int numDevices = scanner.nextInt();
        scanner.nextLine();  // Consumir o newline

        for (int i = 1; i <= numDevices; i++) {
            String jsonBody = String.format(
                    "{\"name\":\"Device %d\", \"type\":\"Type %s\", \"room\":\"Room %d\", \"service\":\"Service %s\", \"floor\":%d, \"building\":\"Building %d\"}",
                    i, getDeviceType(i), i, getService(i), i, (i / 5) + 1); // Atribuindo edifício de forma cíclica

            String url = baseUrl + "/devices";  // Usando baseUrl aqui
            HttpRequest request = HttpClientUtil.buildPostRequest(url, jsonBody);
            HttpResponse<String> response = HttpClientUtil.sendRequest(request);
            System.out.println("Device " + i + " created: " + response.statusCode());
        }
        //scanner.nextLine(); 
        System.out.println("Dump de dispositivos concluído.");
    }

    /**
     * Método privado para obter o tipo de dispositivo baseado no índice.
     * 
     * @param index Índice do dispositivo.
     * @return O tipo do dispositivo.
     */
    private static String getDeviceType(int index) {
        String[] types = {"A", "B", "C"};
        return types[(index - 1) % types.length];
    }

    /**
     * Método privado para obter o serviço baseado no índice.
     * 
     * @param index Índice do dispositivo.
     * @return O serviço do dispositivo.
     */
    private static String getService(int index) {
        String[] services = {"Alpha", "Beta", "Gamma"};
        return services[(index - 1) % services.length];
    }

    /**
     * Método privado para coletar os dados de um novo dispositivo.
     * 
     * @param scanner O scanner usado para interagir com o utilizador.
     * @return O corpo JSON com os dados do dispositivo.
     */
    private static String collectDeviceData(Scanner scanner) {
        System.out.println("Nome do dispositivo:");
        String name = scanner.nextLine();
        System.out.println("Tipo:");
        String type = scanner.nextLine();
        System.out.println("Sala:");
        String room = scanner.nextLine();
        System.out.println("Serviço:");
        String service = scanner.nextLine();
        System.out.println("Piso:");
        String floor = scanner.nextLine();
        System.out.println("Edifício:");
        String building = scanner.nextLine();

        return String.format(
                "{\"name\":\"%s\", \"type\":\"%s\", \"room\":\"%s\", \"service\":\"%s\", \"floor\":%s, \"building\":\"%s\"}",
                name, type, room, service, floor.isEmpty() ? "null" : floor, building);
    }
}
