package com.example.pharmacymanagement.service;

import com.example.pharmacymanagement.dto.PharmacistDto;
import com.example.pharmacymanagement.entity.Location;
import com.example.pharmacymanagement.entity.Pharmacist;
import com.example.pharmacymanagement.exception.ResourceNotFoundException;
import com.example.pharmacymanagement.repository.LocationRepository;
import com.example.pharmacymanagement.repository.PharmacistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PharmacistService {

    private final PharmacistRepository pharmacistRepository;
    private final LocationRepository locationRepository;

    public PharmacistService(PharmacistRepository pharmacistRepository, LocationRepository locationRepository) {
        this.pharmacistRepository = pharmacistRepository;
        this.locationRepository = locationRepository;
    }

    public PharmacistDto createPharmacist(PharmacistDto dto) {
        if (pharmacistRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));

        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setName(dto.getName());
        pharmacist.setEmail(dto.getEmail());
        pharmacist.setPhone(dto.getPhone());
        pharmacist.setLicenseNumber(dto.getLicenseNumber());
        pharmacist.setLocation(location);

        Pharmacist saved = pharmacistRepository.save(pharmacist);
        return mapToDto(saved);
    }

    public List<PharmacistDto> getAllPharmacists() {
        return pharmacistRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public PharmacistDto getPharmacistById(Long id) {
        Pharmacist pharmacist = pharmacistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacist not found"));
        return mapToDto(pharmacist);
    }

    /**
     * Finds all pharmacists belonging to a province identified by code OR name.
     * Accepts either param — passes both to the repository query which uses OR logic.
     */
    public List<PharmacistDto> getPharmacistsByProvince(String provinceCode, String provinceName) {
        return pharmacistRepository.findByProvinceCodeOrName(
                provinceCode != null ? provinceCode : "",
                provinceName != null ? provinceName : "")
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private PharmacistDto mapToDto(Pharmacist p) {
        PharmacistDto dto = new PharmacistDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setEmail(p.getEmail());
        dto.setPhone(p.getPhone());
        dto.setLicenseNumber(p.getLicenseNumber());
        dto.setLocationId(p.getLocation().getId());
        return dto;
    }
}
