package com.iotproject.admin;

import java.util.Scanner;

import com.iotproject.admin.auth.AuthService;
import com.iotproject.admin.device.DeviceService;
import com.iotproject.admin.metric.MetricService;
import com.iotproject.mqtt.MqttSimulator;

/**
 * Classe principal para o cliente administrador do projeto IoT.
 * Oferece um menu interativo para gerenciar dispositivos, consultar métricas e simular mensagens MQTT.
 */
public class AdminClient {
    public static void main(String[] args) throws Exception {
        // Scanner para entrada do usuário via console
        Scanner scanner = new Scanner(System.in);

        // Realiza a autenticação do usuário antes de acessar o menu principal
        if (!AuthService.authenticate(scanner)) {
            System.out.println("Falha na autenticação. Encerrando o programa.");
            scanner.close(); // Fecha o scanner para liberar recursos
            return; // Encerra o programa caso a autenticação falhe
        }

        // URL base para chamadas à API
        String baseUrl = "http://localhost:8080"; // Pode ser configurado conforme necessário

        // Inicializa o simulador MQTT
        MqttSimulator mqttSimulator = new MqttSimulator();

        // Loop principal para exibir o menu e lidar com as opções do usuário
        while (true) {
            showMenu(); // Exibe o menu de opções
            int option = getValidOption(scanner); // Obtém uma entrada válida do usuário

            // Lógica de controle baseada na escolha do usuário
            switch (option) {
                case 1 -> DeviceService.listDevices(scanner, baseUrl); // Lista dispositivos
                case 2 -> DeviceService.createDevice(scanner, baseUrl); // Cria um novo dispositivo
                case 3 -> DeviceService.updateDevice(scanner, baseUrl); // Atualiza um dispositivo existente
                case 4 -> DeviceService.deleteDevice(scanner, baseUrl); // Remove um dispositivo
                case 5 -> MetricService.queryAggregatedMetrics(scanner); // Consulta métricas agregadas
                case 6 -> {
                    // Dump de dispositivos para teste
                    System.out.println("Quantos dispositivos deseja criar?");
                    //getValidOption(scanner); // Número de dispositivos a serem criados
                    DeviceService.dumpDevices(baseUrl, scanner); // Realiza o dump
                }
                case 7 -> {
                    System.out.println("Quantos dispositivos você quer simular?");
                    int numDevices = getValidOption(scanner);
                    mqttSimulator.simulate(numDevices);
                }
                case 8 -> {
                    // Finaliza o programa
                    System.out.println("Saindo...");
                    scanner.close(); // Fecha o scanner antes de sair
                    System.exit(0); // Encerra o programa
                }
                default -> System.out.println("Opção inválida."); // Trata opções não reconhecidas
            }
        }
    }

    /**
     * Exibe o menu principal com as opções disponíveis para o usuário.
     */
    private static void showMenu() {
        System.out.println("\nSelecione uma opção:");
        System.out.println("1. Listar dispositivos");
        System.out.println("2. Criar dispositivo");
        System.out.println("3. Atualizar dispositivo");
        System.out.println("4. Remover dispositivo");
        System.out.println("5. Consultar métricas agregadas");
        System.out.println("6. Dump de dispositivos para teste");
        System.out.println("7. Iniciar simulação MQTT");
        System.out.println("8. Sair");
    }

    /**
     * Obtém uma entrada válida do usuário para as opções do menu.
     * Repete a solicitação até que um número inteiro válido seja fornecido.
     *
     * @param scanner Scanner para leitura da entrada do usuário.
     * @return Número inteiro correspondente à opção escolhida.
     */
    private static int getValidOption(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Digite sua escolha: ");
                return Integer.parseInt(scanner.nextLine()); // Tenta converter a entrada para um inteiro
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número."); // Mensagem de erro para entradas não numéricas
            }
        }
    }
}
