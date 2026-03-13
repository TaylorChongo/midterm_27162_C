package com.example.pharmacymanagement.repository;

import com.example.pharmacymanagement.entity.Location;
import com.example.pharmacymanagement.entity.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCodeAndType(String code, LocationType type);
    Optional<Location> findByNameAndType(String name, LocationType type);
    
    @Query("SELECT l FROM Location l WHERE l.type = :type AND (l.code = :identifier OR l.name = :identifier)")
    Optional<Location> findByTypeAndCodeOrName(@Param("type") LocationType type, @Param("identifier") String identifier);
}
