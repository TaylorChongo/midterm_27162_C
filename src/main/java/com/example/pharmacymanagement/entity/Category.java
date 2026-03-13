package com.example.pharmacymanagement.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a medicine category (e.g., Antibiotics, Analgesics).
 *
 * Relationship:
 *   - Many-to-Many with Medicine: a category can group many medicines,
 *     and a medicine can belong to multiple categories.
 *   - JPA creates a join table "medicine_categories" with columns
 *     medicine_id and category_id to hold these associations.
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    /**
     * Many-to-Many mapping.
     * "mappedBy = 'categories'" means Medicine owns the relationship
     * (Medicine holds the @JoinTable annotation).
     */
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Medicine> medicines = new HashSet<>();

    public Category() {
    }

    public Category(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<Medicine> getMedicines() { return medicines; }
    public void setMedicines(Set<Medicine> medicines) { this.medicines = medicines; }
}
