package com.example.pharmacymanagement.controller;

import com.example.pharmacymanagement.dto.CustomerDto;
import com.example.pharmacymanagement.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer(@RequestBody @Valid CustomerDto dto) {
        return customerService.createCustomer(dto);
    }

    /**
     * GET /customers?page=0&size=10&sort=name,asc
     *
     * Pagination: controlled by page (0-indexed) and size.
     * Sorting: controlled by sort=field,direction (e.g., sort=name,asc).
     * Spring Data JPA's Pageable is resolved automatically from request params.
     */
    @GetMapping
    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        return customerService.getAllCustomers(pageable);
    }

    /**
     * GET /customers/by-province?provinceCode=KGL
     * GET /customers/by-province?provinceName=Kigali
     *
     * Returns all customers in the province matched by code OR name.
     */
    @GetMapping("/by-province")
    public List<CustomerDto> getCustomersByProvince(
            @RequestParam(required = false) String provinceCode,
            @RequestParam(required = false) String provinceName) {
        return customerService.getCustomersByProvince(provinceCode, provinceName);
    }
}
