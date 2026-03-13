package com.example.pharmacymanagement.repository;

import com.example.pharmacymanagement.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
    boolean existsByName(String name);
}
