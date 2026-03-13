package com.example.pharmacymanagement.controller;

import com.example.pharmacymanagement.dto.SaleDto;
import com.example.pharmacymanagement.dto.SaleMedicineDto;
import com.example.pharmacymanagement.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleDto createSale(@RequestBody @Valid SaleDto dto) {
        return saleService.createSale(dto);
    }

    @PostMapping("/{id}/add-medicine")
    public SaleDto addMedicineToSale(@PathVariable Long id, @RequestBody @Valid SaleMedicineDto smDto) {
        return saleService.addMedicineToSale(id, smDto);
    }

    @GetMapping
    public Page<SaleDto> getAllSales(Pageable pageable) {
        return saleService.getAllSales(pageable);
    }
}
