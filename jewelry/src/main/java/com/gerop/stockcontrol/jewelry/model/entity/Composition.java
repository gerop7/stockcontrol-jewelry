package com.gerop.stockcontrol.jewelry.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="compositions")
public class Composition {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=3, max=30)
    private String name;

    @ManyToMany(mappedBy="composition")
    private List<Jewel> jewelry;

    @OneToOne(mappedBy = "composition", cascade = CascadeType.ALL, orphanRemoval = true)
    private PendingCompositionRestock pendingCompositionRestock;

    public Composition() {
        jewelry = new ArrayList<>();
    }

    public Composition(String name) {
        this();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Jewel> getJewelry() {
        return jewelry;
    }

    public void setJewelry(List<Jewel> jewelry) {
        this.jewelry = jewelry;
    }

    public PendingCompositionRestock getPendingCompositionRestock() {
        return pendingCompositionRestock;
    }

    public void setPendingCompositionRestock(PendingCompositionRestock pendingCompositionRestock) {
        this.pendingCompositionRestock = pendingCompositionRestock;
    }

    
}
