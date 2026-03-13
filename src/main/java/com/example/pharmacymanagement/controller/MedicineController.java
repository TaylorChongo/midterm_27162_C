package com.example.pharmacymanagement.controller;

import com.example.pharmacymanagement.dto.MedicineDto;
import com.example.pharmacymanagement.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicineDto createMedicine(@RequestBody @Valid MedicineDto dto) {
        return medicineService.createMedicine(dto);
    }

    @PutMapping("/{id}")
    public MedicineDto updateMedicine(@PathVariable Long id, @RequestBody @Valid MedicineDto dto) {
        return medicineService.updateMedicine(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
    }

    @GetMapping
    public Page<MedicineDto> getAllMedicines(Pageable pageable) {
        return medicineService.getAllMedicines(pageable);
    }
}
