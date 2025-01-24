package com.iotproject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iotproject.model.Device;
import com.iotproject.service.DeviceService;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * Método para obter todos os dispositivos.
     * 
     * @return Lista de dispositivos.
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Usuários com os papéis 'USER' ou 'ADMIN' podem acessar
    @GetMapping
    public List<Device> getDevices() {
        return deviceService.getAllDevices();
    }
    
    /**
     * Método para obter um dispositivo pelo seu ID.
     * 
     * @param id ID do dispositivo.
     * @return Resposta com o dispositivo encontrado ou mensagem de erro.
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Usuários autenticados com os papéis 'USER' ou 'ADMIN'
    @GetMapping("/{id}")
    public ResponseEntity<?> getDeviceById(@PathVariable Long id) {
        Optional<Device> device = Optional.ofNullable(deviceService.getDeviceById(id));
        if (device.isPresent()) {
            return ResponseEntity.ok(device.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Dispositivo com ID " + id + " não encontrado.");
        }
    }

    /**
     * Método para criar um novo dispositivo.
     * 
     * @param device Dados do dispositivo a ser criado.
     * @return Resposta com o dispositivo criado ou mensagem de erro.
     */
    @PreAuthorize("hasRole('ADMIN')") // Apenas administradores podem criar dispositivos
    @PostMapping
    public ResponseEntity<?> createDevice(@RequestBody Device device) {
        if (!validateDevice(device, false)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados do dispositivo inválidos. Verifique os campos obrigatórios.");
        }

        Device savedDevice = deviceService.saveDevice(device);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDevice);
    }

    /**
     * Método para atualizar os dados de um dispositivo existente.
     * 
     * @param id ID do dispositivo a ser atualizado.
     * @param deviceDetails Dados atualizados do dispositivo.
     * @return Resposta com o dispositivo atualizado ou mensagem de erro.
     */
    @PreAuthorize("hasRole('ADMIN')") // Apenas administradores podem atualizar dispositivos
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDevice(@PathVariable Long id, @RequestBody Device deviceDetails) {
        if (!validateDevice(deviceDetails, true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados do dispositivo inválidos. Verifique os campos fornecidos.");
        }

        Device updatedDevice = deviceService.updateDevice(id, deviceDetails);
        return ResponseEntity.ok(updatedDevice);
    }

    /**
 * Método para excluir um dispositivo.
 * 
 * @param id ID do dispositivo a ser excluído.
 * @return Resposta de sucesso ou erro.
 */
@PreAuthorize("hasRole('ADMIN')") // Apenas administradores podem excluir dispositivos
@DeleteMapping("/{id}")
public ResponseEntity<?> deleteDevice(@PathVariable Long id) {
    try {
        deviceService.deleteDevice(id); // Supondo que o serviço exclui corretamente o dispositivo
        return ResponseEntity.status(HttpStatus.OK).body("Dispositivo excluído com sucesso.");
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dispositivo não encontrado: " + e.getMessage());
    }
}

    /**
     * Método para validar os dados de um dispositivo, tanto para criação como para atualização.
     * 
     * @param device Dispositivo a ser validado.
     * @param isUpdate Indica se é para validação de atualização.
     * @return Retorna true se os dados do dispositivo forem válidos, caso contrário retorna false.
     */
    private boolean validateDevice(Device device, boolean isUpdate) {
        if (!isUpdate) {
            // Validação para criação
            return device.getName() != null && !device.getName().isEmpty()
                    && device.getType() != null && !device.getType().isEmpty()
                    && (device.getFloor() != 0 && device.getFloor() >= 0)  // Para criação, floor não pode ser null e deve ser >= 0
                    && device.getBuilding() != null && !device.getBuilding().isEmpty();
        } else {
            // Para atualização, apenas os campos fornecidos devem ser validados
            boolean isNameValid = device.getName() == null || !device.getName().isEmpty();  // Nome é opcional para update
            boolean isTypeValid = device.getType() == null || !device.getType().isEmpty();  // Tipo é opcional para update
            boolean isFloorValid = device.getFloor() == 0 || device.getFloor() >= 0; // Piso é opcional para update
            boolean isBuildingValid = device.getBuilding() == null || !device.getBuilding().isEmpty();  // Edifício é opcional para update
    
            // Retorna true se todos os campos fornecidos ou não fornecidos estiverem válidos
            return isNameValid && isTypeValid && isFloorValid && isBuildingValid;
        }
    }
}
