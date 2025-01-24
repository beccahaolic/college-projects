package com.iotproject.mqtt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

/**
 * Classe responsável por simular a publicação de mensagens MQTT 
 * representando sensores de dispositivos hospitalares.
 */
@Service
public class MqttSimulator {

    private static final String BROKER_URL = "tcp://localhost:1883"; // URL do broker MQTT
    private static final String TOPIC = "hospital/sensors"; // Tópico onde as mensagens serão publicadas

    /**
     * Método que inicia a simulação de dispositivos publicando mensagens MQTT.
     */
    public void simulate() {
        // Scanner para entrada do número de dispositivos a serem simulados
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Quantos dispositivos você quer simular? ");
            int numDevices = scanner.nextInt();

        try (MqttClient client = new MqttClient(BROKER_URL, MqttClient.generateClientId())) {
            // Conecta ao broker MQTT
            client.connect();

            Random random = new Random(); // Gerador de números aleatórios para temperatura e umidade
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Formato de timestamp

            for (int i = 1; i <= numDevices; i++) {
                // Gera o timestamp atual
                String timestamp = LocalDateTime.now().format(formatter);

                // Cria a mensagem JSON simulando os dados de um dispositivo
                String payload = String.format(
                        "{\"deviceId\":\"%d\", \"temperature\":%.2f, \"humidity\":%.2f, \"timestamp\":\"%s\"}",
                        i, random.nextDouble() * 100, random.nextDouble() * 100, timestamp);

                // Configura e publica a mensagem MQTT
                MqttMessage message = new MqttMessage(payload.getBytes());
                client.publish(TOPIC, message);
                System.out.println("Message published: " + payload);

                Thread.sleep(1000); // Aguarda 1 segundo antes de enviar a próxima mensagem
            }

                client.disconnect(); // Desconecta do broker MQTT
            } // Scanner is closed automatically here
        } catch (MqttException | InterruptedException e) {
            // Trata possíveis exceções durante a conexão, publicação ou interrupção
            e.printStackTrace();
        }
    }

    /**
     * Método principal para executar o simulador de forma independente.
     */
    public static void main(String[] args) {
        MqttSimulator simulator = new MqttSimulator();
        simulator.simulate();
    }
}
