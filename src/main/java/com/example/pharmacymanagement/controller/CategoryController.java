package com.example.pharmacymanagement.controller;

import com.example.pharmacymanagement.dto.CategoryDto;
import com.example.pharmacymanagement.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto dto) {
        return categoryService.createCategory(dto);
    }

    @GetMapping
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryService.getAllCategories(pageable);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    /**
     * Assigns a medicine to a category (inserts a row into the join table medicine_categories).
     * Example: POST /categories/1/medicines/2
     */
    @PostMapping("/{categoryId}/medicines/{medicineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignMedicine(@PathVariable Long categoryId, @PathVariable Long medicineId) {
        categoryService.assignMedicineToCategory(medicineId, categoryId);
    }

    /**
     * Removes a medicine from a category.
     * Example: DELETE /categories/1/medicines/2
     */
    @DeleteMapping("/{categoryId}/medicines/{medicineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMedicine(@PathVariable Long categoryId, @PathVariable Long medicineId) {
        categoryService.removeMedicineFromCategory(medicineId, categoryId);
    }
}
