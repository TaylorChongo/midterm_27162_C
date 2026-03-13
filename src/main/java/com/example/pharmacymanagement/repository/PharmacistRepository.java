package com.example.pharmacymanagement.repository;

import com.example.pharmacymanagement.entity.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {

    /**
     * existsByEmail — checks whether a pharmacist with the given email already exists.
     * Spring Data JPA auto-generates the query from the method name:
     *   SELECT COUNT(*) > 0 FROM pharmacists WHERE email = ?
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves all pharmacists whose location belongs (directly or via parent hierarchy)
     * to a province identified by either its CODE or its NAME.
     *
     * Uses explicit LEFT JOINs for each hierarchy level (l1–l4) so that locations
     * with fewer than 4 parent levels are NOT excluded — a missing parent simply
     * yields NULL rather than dropping the entire row (which INNER JOIN would do).
     *
     * Hierarchy levels:
     *   l0 = pharmacist.location          (e.g. commune)
     *   l1 = l0.parentLocation            (e.g. city)
     *   l2 = l1.parentLocation            (e.g. province ← typically matched here)
     *   l3 = l2.parentLocation            (e.g. country)
     *   l4 = l3.parentLocation            (top — null for most hierarchies)
     *
     * The WHERE checks each level: does ANY ancestor match provinceCode OR provinceName?
     *
     * @param provinceCode short code, e.g. "KGL"
     * @param provinceName full name,  e.g. "Kigali"
     */
    @Query("SELECT DISTINCT p FROM Pharmacist p " +
           "LEFT JOIN p.location l0 " +
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
    List<Pharmacist> findByProvinceCodeOrName(@Param("provinceCode") String provinceCode,
                                              @Param("provinceName") String provinceName);
}
