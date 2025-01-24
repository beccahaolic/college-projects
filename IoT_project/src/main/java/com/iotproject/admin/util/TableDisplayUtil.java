package com.iotproject.admin.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TableDisplayUtil {

    /**
     * Exibe dispositivos em formato de tabela no console.
     * Calcula a largura das colunas com base nos dados e imprime as informações.
     *
     * @param devices A lista de dispositivos (formato JsonArray).
     */
    public static void displayDevicesAsTable(JsonArray devices) {
        if (devices.size() == 0) {
            System.out.println("Nenhum dispositivo encontrado.");
            return;
        }

        // Supondo que todos os dispositivos tenham o mesmo formato, pegamos as chaves do primeiro
        JsonObject firstDevice = devices.get(0).getAsJsonObject();
        var keys = firstDevice.keySet();
        int[] columnWidths = new int[keys.size()];
        int i = 0;

        // Calcula a largura máxima para cada coluna
        for (var device : devices) {
            JsonObject jsonObject = device.getAsJsonObject();
            int columnIndex = 0;
            for (String key : keys) {
                String value = jsonObject.has(key) ? jsonObject.get(key).getAsString() : "N/A";
                columnWidths[columnIndex] = Math.max(columnWidths[columnIndex], value.length());
                columnIndex++;
            }
        }

        // Imprime o cabeçalho da tabela
        StringBuilder header = new StringBuilder();
        StringBuilder separator = new StringBuilder();
        i = 0;
        for (String key : keys) {
            header.append(String.format("%-" + columnWidths[i] + "s ", key.toUpperCase()));
            separator.append("-".repeat(columnWidths[i] + 1));
            i++;
        }
        System.out.println(header);
        System.out.println(separator);

        // Imprime as linhas de dados
        for (var device : devices) {
            JsonObject jsonObject = device.getAsJsonObject();
            StringBuilder row = new StringBuilder();
            int columnIndex = 0;
            for (String key : keys) {
                String value = jsonObject.has(key) ? jsonObject.get(key).getAsString() : "N/A";
                row.append(String.format("%-" + columnWidths[columnIndex] + "s ", value));
                columnIndex++;
            }
            System.out.println(row);
        }
    }

    /**
     * Exibe métricas em formato de tabela no console.
     * Calcula a largura das colunas com base nos dados e imprime as informações.
     *
     * @param metrics A lista de métricas (formato JsonArray).
     */
    public static void displayMetricsAsTable(JsonArray metrics) {
        if (metrics.size() == 0) {
            System.out.println("Nenhuma métrica encontrada.");
            return;
        }

        String[] headers = {"ID", "Temperature (°C)", "      Humidity (%)"};
        int numColumns = headers.length;

        int[] columnWidths = new int[numColumns];

        // Calcula a largura máxima para cada coluna
        for (var metric : metrics) {
            JsonArray values = metric.getAsJsonArray();
            for (int i = 0; i < numColumns; i++) {
                String value = values.get(i).getAsString();
                columnWidths[i] = Math.max(columnWidths[i], value.length());
            }
        }

        // Imprime o cabeçalho da tabela
        StringBuilder header = new StringBuilder();
        StringBuilder separator = new StringBuilder();
        for (int i = 0; i < numColumns; i++) {
            header.append(String.format("%-" + columnWidths[i] + "s ", headers[i]));
            separator.append("-".repeat(columnWidths[i] + 1));
        }
        System.out.println(header);
        System.out.println(separator);

        // Imprime as linhas de dados
        for (var metric : metrics) {
            JsonArray values = metric.getAsJsonArray();
            StringBuilder row = new StringBuilder();
            for (int i = 0; i < numColumns; i++) {
                String value = values.get(i).getAsString();
                row.append(String.format("%-" + columnWidths[i] + "s ", value));
            }
            System.out.println(row);
        }
    }
}
