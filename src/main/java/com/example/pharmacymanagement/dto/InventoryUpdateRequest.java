package com.example.pharmacymanagement.dto;

import jakarta.validation.constraints.NotNull;

public class InventoryUpdateRequest {
    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotNull(message = "Quantity to add is required")
    private Integer quantityToAdd;

    public InventoryUpdateRequest() {
    }

    public InventoryUpdateRequest(Long medicineId, Integer quantityToAdd) {
        this.medicineId = medicineId;
        this.quantityToAdd = quantityToAdd;
    }

    public Long getMedicineId() { return medicineId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
    
    public Integer getQuantityToAdd() { return quantityToAdd; }
    public void setQuantityToAdd(Integer quantityToAdd) { this.quantityToAdd = quantityToAdd; }
}
