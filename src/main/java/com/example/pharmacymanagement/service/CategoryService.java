package com.example.pharmacymanagement.service;

import com.example.pharmacymanagement.dto.CategoryDto;
import com.example.pharmacymanagement.entity.Category;
import com.example.pharmacymanagement.entity.Medicine;
import com.example.pharmacymanagement.exception.ResourceNotFoundException;
import com.example.pharmacymanagement.repository.CategoryRepository;
import com.example.pharmacymanagement.repository.MedicineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MedicineRepository medicineRepository;

    public CategoryService(CategoryRepository categoryRepository, MedicineRepository medicineRepository) {
        this.categoryRepository = categoryRepository;
        this.medicineRepository = medicineRepository;
    }

    public CategoryDto createCategory(CategoryDto dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Category already exists: " + dto.getName());
        }
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return mapToDto(categoryRepository.save(category));
    }

    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(this::mapToDto);
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return mapToDto(category);
    }

    /**
     * Assigns an existing medicine to an existing category.
     * This updates the join table "medicine_categories".
     * Since Medicine owns the relationship, we add the category to medicine.categories.
     */
    @Transactional
    public void assignMedicineToCategory(Long medicineId, Long categoryId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        medicine.getCategories().add(category);
        medicineRepository.save(medicine);
    }

    /**
     * Removes a medicine from a category (deletes the join table row).
     */
    @Transactional
    public void removeMedicineFromCategory(Long medicineId, Long categoryId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        medicine.getCategories().remove(category);
        medicineRepository.save(medicine);
    }

    private CategoryDto mapToDto(Category c) {
        return new CategoryDto(c.getId(), c.getName(), c.getDescription());
    }
}
