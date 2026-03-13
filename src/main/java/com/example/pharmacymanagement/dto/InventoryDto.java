package com.example.pharmacymanagement.dto;

public class InventoryDto {
    private Long id;
    private Integer quantity;
    private Long medicineId;
    private String medicineName;

    public InventoryDto() {
    }

    public InventoryDto(Long id, Integer quantity, Long medicineId, String medicineName) {
        this.id = id;
        this.quantity = quantity;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public Long getMedicineId() { return medicineId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
    
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
}
