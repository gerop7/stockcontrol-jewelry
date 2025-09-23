package com.gerop.stockcontrol.jewelry.model.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="subcategories")
public class Subcategory {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=3, max=30)
    private String name;

    @OneToMany(mappedBy="subcategory")
    private List<Jewel> jewelry;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Category principalCategory;

    public Subcategory() {
    }

    public Subcategory(String name) {
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

    public Category getPrincipalCategory() {
        return principalCategory;
    }

    public void setPrincipalCategory(Category principalCategory) {
        this.principalCategory = principalCategory;
    }

    
}
