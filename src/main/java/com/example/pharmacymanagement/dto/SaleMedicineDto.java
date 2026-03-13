package com.example.pharmacymanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SaleMedicineDto {
    
    @NotNull(message = "Medicine ID is required")
    private Long medicineId;
    
    private String medicineName;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    private Double priceAtSale;

    public SaleMedicineDto() {
    }

    public SaleMedicineDto(Long medicineId, String medicineName, Integer quantity, Double priceAtSale) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.priceAtSale = priceAtSale;
    }

    public Long getMedicineId() { return medicineId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
    
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public Double getPriceAtSale() { return priceAtSale; }
    public void setPriceAtSale(Double priceAtSale) { this.priceAtSale = priceAtSale; }
}
