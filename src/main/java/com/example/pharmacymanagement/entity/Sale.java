package com.example.pharmacymanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime saleDate;

    @Column(nullable = false)
    private Double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacist_id", nullable = false)
    private Pharmacist pharmacist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleMedicine> saleMedicines = new ArrayList<>();

    public Sale() {
    }

    public Sale(Long id, LocalDateTime saleDate, Double totalAmount, Pharmacist pharmacist, Customer customer, List<SaleMedicine> saleMedicines) {
        this.id = id;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.pharmacist = pharmacist;
        this.customer = customer;
        this.saleMedicines = saleMedicines != null ? saleMedicines : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Pharmacist getPharmacist() { return pharmacist; }
    public void setPharmacist(Pharmacist pharmacist) { this.pharmacist = pharmacist; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<SaleMedicine> getSaleMedicines() { return saleMedicines; }
    public void setSaleMedicines(List<SaleMedicine> saleMedicines) { this.saleMedicines = saleMedicines; }
}
