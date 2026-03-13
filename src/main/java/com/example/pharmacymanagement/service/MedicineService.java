package com.example.pharmacymanagement.service;

import com.example.pharmacymanagement.dto.MedicineDto;
import com.example.pharmacymanagement.entity.Category;
import com.example.pharmacymanagement.entity.Inventory;
import com.example.pharmacymanagement.entity.Medicine;
import com.example.pharmacymanagement.exception.ResourceNotFoundException;
import com.example.pharmacymanagement.repository.CategoryRepository;
import com.example.pharmacymanagement.repository.InventoryRepository;
import com.example.pharmacymanagement.repository.MedicineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final InventoryRepository inventoryRepository;
    private final CategoryRepository categoryRepository;

    public MedicineService(MedicineRepository medicineRepository,
                           InventoryRepository inventoryRepository,
                           CategoryRepository categoryRepository) {
        this.medicineRepository = medicineRepository;
        this.inventoryRepository = inventoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public MedicineDto createMedicine(MedicineDto dto) {
        if (medicineRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Medicine already exists");
        }

        Medicine medicine = new Medicine();
        medicine.setName(dto.getName());
        medicine.setManufacturer(dto.getManufacturer());
        medicine.setPrice(dto.getPrice());
        medicine.setExpirationDate(dto.getExpirationDate());

        // Resolve and assign categories if provided (Many-to-Many)
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));
            medicine.setCategories(categories);
        }

        Medicine saved = medicineRepository.save(medicine);

        // Also create empty inventory whenever a medicine is created (One-to-One)
        Inventory inventory = new Inventory();
        inventory.setMedicine(saved);
        inventory.setQuantity(0);
        inventoryRepository.save(inventory);

        return mapToDto(saved);
    }

    @Transactional
    public MedicineDto updateMedicine(Long id, MedicineDto dto) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        medicine.setName(dto.getName());
        medicine.setManufacturer(dto.getManufacturer());
        medicine.setPrice(dto.getPrice());
        medicine.setExpirationDate(dto.getExpirationDate());

        if (dto.getCategoryIds() != null) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));
            medicine.setCategories(categories);
        }

        return mapToDto(medicineRepository.save(medicine));
    }

    public void deleteMedicine(Long id) {
        if (!medicineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medicine not found");
        }
        medicineRepository.deleteById(id);
    }

    public Page<MedicineDto> getAllMedicines(Pageable pageable) {
        return medicineRepository.findAll(pageable).map(this::mapToDto);
    }

    private MedicineDto mapToDto(Medicine m) {
        MedicineDto dto = new MedicineDto();
        dto.setId(m.getId());
        dto.setName(m.getName());
        dto.setManufacturer(m.getManufacturer());
        dto.setPrice(m.getPrice());
        dto.setExpirationDate(m.getExpirationDate());
        if (m.getCategories() != null) {
            dto.setCategoryIds(m.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }
}
