package com.iotproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iotproject.model.Device;
import com.iotproject.repository.DeviceRepository;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    // Obter todos os dispositivos
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    // Obter um dispositivo por ID
    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Dispositivo não encontrado com ID: " + id)
        );
    }

    // Salvar um novo dispositivo
    public Device saveDevice(Device device) {
        // Verifica se já existe um dispositivo com o mesmo nome usando o método do repositório
        if (deviceRepository.existsByName(device.getName())) {
            throw new RuntimeException("Já existe um dispositivo com o nome " + device.getName());
        }

        validateDevice(device, false); // Valida os dados para criação
        return deviceRepository.save(device);
    }

    // Atualizar um dispositivo existente
    public Device updateDevice(Long id, Device deviceDetails) {
        Device existingDevice = deviceRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Dispositivo não encontrado com ID: " + id)
        );

        // Verifica se o novo nome já está em uso, mas ignora o próprio dispositivo sendo atualizado
        if (deviceDetails.getName() != null && !deviceDetails.getName().equals(existingDevice.getName())) {
            if (deviceRepository.existsByName(deviceDetails.getName())) {
                throw new RuntimeException("Já existe um dispositivo com o nome " + deviceDetails.getName());
            }
        }

        validateDevice(deviceDetails, true); // Valida os dados para atualização

        // Atualiza apenas os campos fornecidos no request
        if (deviceDetails.getName() != null && !deviceDetails.getName().isEmpty()) {
            existingDevice.setName(deviceDetails.getName());
        }
        if (deviceDetails.getType() != null && !deviceDetails.getType().isEmpty()) {
            existingDevice.setType(deviceDetails.getType());
        }
        if (deviceDetails.getRoom() != null && !deviceDetails.getRoom().isEmpty()) {
            existingDevice.setRoom(deviceDetails.getRoom());
        }
        if (deviceDetails.getService() != null && !deviceDetails.getService().isEmpty()) {
            existingDevice.setService(deviceDetails.getService());
        }
        if (deviceDetails.getFloor() != null) {
            existingDevice.setFloor(deviceDetails.getFloor());
        }
        if (deviceDetails.getBuilding() != null && !deviceDetails.getBuilding().isEmpty()) {
            existingDevice.setBuilding(deviceDetails.getBuilding());
        }

        return deviceRepository.save(existingDevice);
    }

    // Deletar um dispositivo
    public void deleteDevice(Long id) {
        Device existingDevice = deviceRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Dispositivo não encontrado com ID: " + id)
        );
        deviceRepository.delete(existingDevice);
    }

    // Validação de dados do dispositivo
    private boolean validateDevice(Device device, boolean isUpdate) {
        if (device == null) {
            throw new IllegalArgumentException("O dispositivo não pode ser nulo.");
        }
        if (!isUpdate) {
            // Validação para criação (campos obrigatórios)
            if (device.getName() == null || device.getName().isEmpty()) {
                throw new IllegalArgumentException("O nome do dispositivo é obrigatório.");
            }
            if (device.getType() == null || device.getType().isEmpty()) {
                throw new IllegalArgumentException("O tipo do dispositivo é obrigatório.");
            }
            if (device.getRoom() == null || device.getRoom().isEmpty()) {
                throw new IllegalArgumentException("A sala do dispositivo é obrigatória.");
            }
            if (device.getFloor() == null) {
                throw new IllegalArgumentException("O andar do dispositivo é obrigatório.");
            }
            if (device.getBuilding() == null || device.getBuilding().isEmpty()) {
                throw new IllegalArgumentException("O edifício do dispositivo é obrigatório.");
            }
        } else {
            // Validação para atualização (campos podem ser parciais, mas devem ser válidos se fornecidos)
            if (device.getName() != null && device.getName().isEmpty()) {
                throw new IllegalArgumentException("O nome do dispositivo não pode ser vazio.");
            }
            if (device.getType() != null && device.getType().isEmpty()) {
                throw new IllegalArgumentException("O tipo do dispositivo não pode ser vazio.");
            }
            if (device.getRoom() != null && device.getRoom().isEmpty()) {
                throw new IllegalArgumentException("A sala do dispositivo não pode ser vazia.");
            }
            if (device.getBuilding() != null && device.getBuilding().isEmpty()) {
                throw new IllegalArgumentException("O edifício do dispositivo não pode ser vazio.");
            }
        }
        return true;
    }
}
