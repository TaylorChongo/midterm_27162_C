package com.example.pharmacymanagement.repository;

import com.example.pharmacymanagement.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Retrieves all customers whose location belongs to a province
     * identified by either its CODE or its NAME.
     *
     * Uses explicit LEFT JOINs so locations with fewer than 4 parent levels
     * are still considered (NULL parent → NULL join, which is harmless in a WHERE OR chain).
     */
    @Query("SELECT DISTINCT c FROM Customer c " +
           "LEFT JOIN c.location l0 " +
           "LEFT JOIN l0.parentLocation l1 " +
           "LEFT JOIN l1.parentLocation l2 " +
           "LEFT JOIN l2.parentLocation l3 " +
           "LEFT JOIN l3.parentLocation l4 " +
           "WHERE " +
           "  (l0.code = :provinceCode OR l0.name = :provinceName) OR " +
           "  (l1.code = :provinceCode OR l1.name = :provinceName) OR " +
           "  (l2.code = :provinceCode OR l2.name = :provinceName) OR " +
           "  (l3.code = :provinceCode OR l3.name = :provinceName) OR " +
           "  (l4.code = :provinceCode OR l4.name = :provinceName)")
    List<Customer> findByProvinceCodeOrName(@Param("provinceCode") String provinceCode,
                                            @Param("provinceName") String provinceName);
}
