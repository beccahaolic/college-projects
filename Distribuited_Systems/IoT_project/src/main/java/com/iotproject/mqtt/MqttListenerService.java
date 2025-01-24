package com.iotproject.mqtt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iotproject.model.Device;
import com.iotproject.model.Metrics;
import com.iotproject.repository.DeviceRepository;
import com.iotproject.repository.MetricsRepository;

import jakarta.annotation.PostConstruct;

/**
 * Serviço responsável por ouvir mensagens MQTT no tópico "hospital/sensors",
 * processar os dados recebidos e armazenar métricas relacionadas aos dispositivos.
 */
@Service
public class MqttListenerService {

    @Autowired
    private DeviceRepository deviceRepository; // Repositório para gerenciar dispositivos

    @Autowired
    private MetricsRepository metricsRepository; // Repositório para gerenciar métricas

    private final ObjectMapper objectMapper = new ObjectMapper(); // Para processar JSON
    private MqttClient client; // Cliente MQTT para conexão e assinaturas

    /**
     * Construtor padrão.
     * Pode ser usado para inicializações básicas se necessário.
     */
    public MqttListenerService() {
        System.out.println("MqttListenerService initialized");
    }

    /**
     * Método anotado com @PostConstruct para inicializar e assinar o tópico MQTT
     * após a criação do bean Spring.
     */
    @PostConstruct
    public void subscribe() {
        try {
            System.out.println("Initializing MQTT Listener...");
            // Configuração do cliente MQTT
            client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    // Reação à perda de conexão
                    System.out.println("Connection lost: " + cause.getMessage());
                    reconnect();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    // Processa a mensagem recebida
                    String payload = new String(message.getPayload());
                    System.out.println("Received message: " + payload);
                    processMessage(payload);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Não utilizado neste cenário, pois é um listener
                }
            });
            client.connect(); // Conecta ao broker MQTT
            client.subscribe("hospital/sensors"); // Assina o tópico "hospital/sensors"
            System.out.println("MQTT Listener subscribed to topic 'hospital/sensors'.");
        } catch (MqttException e) {
            System.err.println("Error initializing MQTT Listener: " + e.getMessage());
        }
    }

    /**
     * Método para reconectar ao broker MQTT em caso de desconexão.
     */
    private void reconnect() {
        int attempts = 0;
        while (!client.isConnected() && attempts < 5) {
            try {
                System.out.println("Attempting to reconnect...");
                client.reconnect(); // Tenta reconectar
                client.subscribe("hospital/sensors"); // Reassina o tópico
                System.out.println("Reconnected to MQTT broker.");
                return;
            } catch (MqttException e) {
                System.err.println("Failed to reconnect: " + e.getMessage());
                attempts++;
                try {
                    Thread.sleep(5000); // Aguarda 5 segundos antes de tentar novamente
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.err.println("Failed to reconnect after 5 attempts.");
    }

    /**
     * Processa mensagens recebidas no formato JSON, verificando campos obrigatórios
     * e salvando métricas no banco de dados.
     *
     * @param payload Mensagem recebida no formato JSON.
     */
    private void processMessage(String payload) {
        try {
            // Substitui vírgulas por pontos em números (caso necessário)
            payload = payload.replaceAll("(\\d),(\\d)", "$1.$2");

            // Parse do JSON
            JsonNode jsonNode = objectMapper.readTree(payload);

            // Extração do ID do dispositivo
            String deviceId = jsonNode.has("deviceId") ? jsonNode.get("deviceId").asText() : null;
            if (deviceId == null || deviceId.trim().isEmpty()) {
                throw new IllegalArgumentException("Missing or invalid deviceId in payload: " + payload);
            }

            // Extração de temperatura e umidade
            double temperature = jsonNode.has("temperature") ? jsonNode.get("temperature").asDouble() : Double.NaN;
            double humidity = jsonNode.has("humidity") ? jsonNode.get("humidity").asDouble() : Double.NaN;

            if (Double.isNaN(temperature) || Double.isNaN(humidity)) {
                throw new IllegalArgumentException("Missing or invalid temperature or humidity in payload: " + payload);
            }

            // Processamento do timestamp
            LocalDateTime timestamp = null;
            try {
                timestamp = LocalDateTime.parse(
                        jsonNode.get("timestamp").asText(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                );
            } catch (Exception e) {
                System.err.println("Invalid timestamp format: " + jsonNode.get("timestamp").asText());
                return;
            }

            // Verificação de existência do dispositivo
            Optional<Device> deviceOptional = deviceRepository.findById(Long.parseLong(deviceId));
            Device device = deviceOptional.orElse(null);
            if (device == null) {
                System.out.println("Device not found. Skipping message: " + payload);
                return;
            }

            // Criação e salvamento da métrica
            Metrics metrics = new Metrics(temperature, humidity, timestamp, device);
            metricsRepository.save(metrics);

            System.out.println("Metrics saved: " + metrics);
        } catch (Exception e) {
            System.err.println("Error processing message: " + payload);
            e.printStackTrace();
        }
    }
}
