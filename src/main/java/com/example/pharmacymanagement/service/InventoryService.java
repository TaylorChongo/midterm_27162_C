package com.example.pharmacymanagement.service;

import com.example.pharmacymanagement.dto.InventoryDto;
import com.example.pharmacymanagement.dto.InventoryUpdateRequest;
import com.example.pharmacymanagement.entity.Inventory;
import com.example.pharmacymanagement.exception.ResourceNotFoundException;
import com.example.pharmacymanagement.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public InventoryDto updateInventory(InventoryUpdateRequest request) {
        Inventory inventory = inventoryRepository.findByMedicineId(request.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory for medicine not found"));

        inventory.setQuantity(inventory.getQuantity() + request.getQuantityToAdd());
        Inventory saved = inventoryRepository.save(inventory);
        return mapToDto(saved);
    }

    public InventoryDto getInventoryByMedicineId(Long medicineId) {
        Inventory inventory = inventoryRepository.findByMedicineId(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory for medicine not found"));
        return mapToDto(inventory);
    }

    private InventoryDto mapToDto(Inventory inv) {
        InventoryDto dto = new InventoryDto();
        dto.setId(inv.getId());
        dto.setQuantity(inv.getQuantity());
        dto.setMedicineId(inv.getMedicine().getId());
        dto.setMedicineName(inv.getMedicine().getName());
        return dto;
    }
}
