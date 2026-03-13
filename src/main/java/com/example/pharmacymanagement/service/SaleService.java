package com.example.pharmacymanagement.service;

import com.example.pharmacymanagement.dto.SaleDto;
import com.example.pharmacymanagement.dto.SaleMedicineDto;
import com.example.pharmacymanagement.entity.*;
import com.example.pharmacymanagement.exception.ResourceNotFoundException;
import com.example.pharmacymanagement.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final PharmacistRepository pharmacistRepository;
    private final CustomerRepository customerRepository;
    private final MedicineRepository medicineRepository;
    private final InventoryRepository inventoryRepository;

    public SaleService(SaleRepository saleRepository, PharmacistRepository pharmacistRepository, CustomerRepository customerRepository, MedicineRepository medicineRepository, InventoryRepository inventoryRepository) {
        this.saleRepository = saleRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.customerRepository = customerRepository;
        this.medicineRepository = medicineRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public SaleDto createSale(SaleDto dto) {
        Pharmacist pharmacist = pharmacistRepository.findById(dto.getPharmacistId())
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacist not found"));

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Sale sale = new Sale();
        sale.setSaleDate(LocalDateTime.now());
        sale.setPharmacist(pharmacist);
        sale.setCustomer(customer);
        sale.setTotalAmount(0.0);
        sale.setSaleMedicines(new ArrayList<>());

        Sale savedSale = saleRepository.save(sale);

        if (dto.getSaleMedicines() != null && !dto.getSaleMedicines().isEmpty()) {
            for (SaleMedicineDto smDto : dto.getSaleMedicines()) {
                addMedicineToSaleEntity(savedSale, smDto);
            }
        }
        
        return mapToDto(saleRepository.save(savedSale));
    }

    @Transactional
    public SaleDto addMedicineToSale(Long saleId, SaleMedicineDto smDto) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        addMedicineToSaleEntity(sale, smDto);
        return mapToDto(saleRepository.save(sale));
    }

    private void addMedicineToSaleEntity(Sale sale, SaleMedicineDto smDto) {
        Medicine medicine = medicineRepository.findById(smDto.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        Inventory inventory = inventoryRepository.findByMedicineId(medicine.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

        if (inventory.getQuantity() < smDto.getQuantity()) {
            throw new IllegalArgumentException("Not enough inventory for medicine: " + medicine.getName());
        }

        // Deduct inventory
        inventory.setQuantity(inventory.getQuantity() - smDto.getQuantity());
        inventoryRepository.save(inventory);

        SaleMedicine saleMedicine = new SaleMedicine();
        saleMedicine.setSale(sale);
        saleMedicine.setMedicine(medicine);
        saleMedicine.setQuantity(smDto.getQuantity());
        saleMedicine.setPriceAtSale(medicine.getPrice());

        sale.getSaleMedicines().add(saleMedicine);
        
        // Update total
        Double currentTotal = sale.getTotalAmount() != null ? sale.getTotalAmount() : 0.0;
        sale.setTotalAmount(currentTotal + (medicine.getPrice() * smDto.getQuantity()));
    }

    public Page<SaleDto> getAllSales(Pageable pageable) {
        return saleRepository.findAll(pageable).map(this::mapToDto);
    }

    private SaleDto mapToDto(Sale s) {
        SaleDto dto = new SaleDto();
        dto.setId(s.getId());
        dto.setSaleDate(s.getSaleDate());
        dto.setTotalAmount(s.getTotalAmount());
        dto.setPharmacistId(s.getPharmacist().getId());
        dto.setCustomerId(s.getCustomer().getId());
        
        if (s.getSaleMedicines() != null) {
            dto.setSaleMedicines(s.getSaleMedicines().stream().map(sm -> {
                SaleMedicineDto smDto = new SaleMedicineDto();
                smDto.setMedicineId(sm.getMedicine().getId());
                smDto.setMedicineName(sm.getMedicine().getName());
                smDto.setQuantity(sm.getQuantity());
                smDto.setPriceAtSale(sm.getPriceAtSale());
                return smDto;
            }).collect(Collectors.toList()));
        }
        
        return dto;
    }
}
