package com.example.pharmacymanagement.controller;

import com.example.pharmacymanagement.dto.InventoryDto;
import com.example.pharmacymanagement.dto.InventoryUpdateRequest;
import com.example.pharmacymanagement.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PutMapping("/update")
    public InventoryDto updateInventory(@RequestBody @Valid InventoryUpdateRequest request) {
        return inventoryService.updateInventory(request);
    }

    @GetMapping("/{medicineId}")
    public InventoryDto getInventoryByMedicineId(@PathVariable Long medicineId) {
        return inventoryService.getInventoryByMedicineId(medicineId);
    }
}
