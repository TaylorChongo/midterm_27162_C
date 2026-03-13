package com.example.pharmacymanagement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class SaleDto {
    private Long id;
    private LocalDateTime saleDate;
    private Double totalAmount;
    
    @NotNull(message = "Pharmacist ID is required")
    private Long pharmacistId;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotEmpty(message = "Sale must contain at least one medicine")
    @Valid
    private List<SaleMedicineDto> saleMedicines;

    public SaleDto() {
    }

    public SaleDto(Long id, LocalDateTime saleDate, Double totalAmount, Long pharmacistId, Long customerId, List<SaleMedicineDto> saleMedicines) {
        this.id = id;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.pharmacistId = pharmacistId;
        this.customerId = customerId;
        this.saleMedicines = saleMedicines;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public Long getPharmacistId() { return pharmacistId; }
    public void setPharmacistId(Long pharmacistId) { this.pharmacistId = pharmacistId; }
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public List<SaleMedicineDto> getSaleMedicines() { return saleMedicines; }
    public void setSaleMedicines(List<SaleMedicineDto> saleMedicines) { this.saleMedicines = saleMedicines; }
}
