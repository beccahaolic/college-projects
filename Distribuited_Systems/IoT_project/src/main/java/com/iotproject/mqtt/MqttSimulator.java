package com.iotproject.mqtt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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
     * 
     * @param numDevices Número de dispositivos a serem simulados.
     */
    public void simulate(int numDevices) {
        if (numDevices <= 0) {
            System.out.println("Número de dispositivos inválido. Simulação não iniciada.");
            return;
        }

        try (MqttClient client = new MqttClient(BROKER_URL, MqttClient.generateClientId())) {
            // Conecta ao broker MQTT
            client.connect();
            System.out.println("Conectado ao broker MQTT: " + BROKER_URL);

            Random random = new Random(); // Gerador de números aleatórios para temperatura e umidade
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Formato de timestamp

            for (int i = 1; i <= numDevices; i++) {
                // Gera o timestamp atual
                String timestamp = LocalDateTime.now().format(formatter);

                // Gera valores aleatórios com precisão limitada
                double temperature = Math.round(random.nextDouble() * 100 * 100) / 100.0;
                double humidity = Math.round(random.nextDouble() * 100 * 100) / 100.0;

                // Cria a mensagem JSON simulando os dados de um dispositivo
                String payload = String.format(
                        "{\"deviceId\":\"%d\", \"temperature\":%.2f, \"humidity\":%.2f, \"timestamp\":\"%s\"}",
                        i, temperature, humidity, timestamp);

                // Configura e publica a mensagem MQTT
                MqttMessage message = new MqttMessage(payload.getBytes());
                message.setQos(1); // Define a qualidade de serviço (QoS) da mensagem
                client.publish(TOPIC, message);
                System.out.println("Message published: " + payload);

                Thread.sleep(1000); // Aguarda 1 segundo antes de enviar a próxima mensagem
            }

            client.disconnect(); // Desconecta do broker MQTT
            System.out.println("Desconectado do broker MQTT.");
        } catch (MqttException e) {
            System.err.println("Erro na comunicação com o broker MQTT: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Simulação interrompida: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restaura o status de interrupção da thread
        }
    }

    /**
     * Método principal para executar o simulador de forma independente.
     */
    public static void main(String[] args) {
        MqttSimulator simulator = new MqttSimulator();
        simulator.simulate(10); // Simula 10 dispositivos como exemplo
    }
}
