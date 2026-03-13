package com.example.pharmacymanagement.repository;

import com.example.pharmacymanagement.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
