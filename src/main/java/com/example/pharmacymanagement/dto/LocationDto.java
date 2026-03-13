package com.example.pharmacymanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class LocationDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    // Optional code (used for provinces, e.g., "KGL", "NGZ")
    private String code;

    @NotBlank(message = "Type is required")
    private String type;

    private Long parentLocationId;

    public LocationDto() {
    }

    public LocationDto(Long id, String name, String code, String type, Long parentLocationId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.type = type;
        this.parentLocationId = parentLocationId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getParentLocationId() { return parentLocationId; }
    public void setParentLocationId(Long parentLocationId) { this.parentLocationId = parentLocationId; }
}
