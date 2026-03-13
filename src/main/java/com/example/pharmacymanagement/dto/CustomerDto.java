package com.example.pharmacymanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class CustomerDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone is required")
    private String phone;

    private Long locationId;
    
    // Alternative: specify village by code or name
    private String villageIdentifier;

    public CustomerDto() {
    }

    public CustomerDto(Long id, String name, String phone, Long locationId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.locationId = locationId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    
    public String getVillageIdentifier() { return villageIdentifier; }
    public void setVillageIdentifier(String villageIdentifier) { this.villageIdentifier = villageIdentifier; }
}
