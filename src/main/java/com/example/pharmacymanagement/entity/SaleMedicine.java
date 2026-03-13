package com.example.pharmacymanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sale_medicines")
public class SaleMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double priceAtSale;

    public SaleMedicine() {
    }

    public SaleMedicine(Long id, Sale sale, Medicine medicine, Integer quantity, Double priceAtSale) {
        this.id = id;
        this.sale = sale;
        this.medicine = medicine;
        this.quantity = quantity;
        this.priceAtSale = priceAtSale;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }

    public Medicine getMedicine() { return medicine; }
    public void setMedicine(Medicine medicine) { this.medicine = medicine; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getPriceAtSale() { return priceAtSale; }
    public void setPriceAtSale(Double priceAtSale) { this.priceAtSale = priceAtSale; }
}
