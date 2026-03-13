package com.example.pharmacymanagement.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")
public class Location {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Short code for the location (e.g., province code like "KGL", "NGZ")
    @Column(unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LocationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Location parentLocation;

    @OneToMany(mappedBy = "parentLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> children = new ArrayList<>();

    public Location() {
    }

    public Location(Long id, String name, String code, LocationType type, Location parentLocation, List<Location> children) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.type = type;
        this.parentLocation = parentLocation;
        this.children = children != null ? children : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public LocationType getType() { return type; }
    public void setType(LocationType type) { this.type = type; }

    public Location getParentLocation() { return parentLocation; }
    public void setParentLocation(Location parentLocation) { this.parentLocation = parentLocation; }

    public List<Location> getChildren() { return children; }
    public void setChildren(List<Location> children) { this.children = children; }
}
