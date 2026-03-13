package com.example.pharmacymanagement.controller;

import com.example.pharmacymanagement.dto.PharmacistDto;
import com.example.pharmacymanagement.service.PharmacistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pharmacists")
public class PharmacistController {

    private final PharmacistService pharmacistService;

    public PharmacistController(PharmacistService pharmacistService) {
        this.pharmacistService = pharmacistService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PharmacistDto createPharmacist(@RequestBody @Valid PharmacistDto dto) {
        return pharmacistService.createPharmacist(dto);
    }

    @GetMapping
    public List<PharmacistDto> getAllPharmacists() {
        return pharmacistService.getAllPharmacists();
    }

    @GetMapping("/{id}")
    public PharmacistDto getPharmacistById(@PathVariable Long id) {
        return pharmacistService.getPharmacistById(id);
    }

    /**
     * GET /pharmacists/by-province?provinceCode=KGL
     * GET /pharmacists/by-province?provinceName=Kigali
     * GET /pharmacists/by-province?provinceCode=KGL&provinceName=Kigali
     *
     * Returns all pharmacists in the given province, matched by code OR name.
     */
    @GetMapping("/by-province")
    public List<PharmacistDto> getPharmacistsByProvince(
            @RequestParam(required = false) String provinceCode,
            @RequestParam(required = false) String provinceName) {
        return pharmacistService.getPharmacistsByProvince(provinceCode, provinceName);
    }
}
