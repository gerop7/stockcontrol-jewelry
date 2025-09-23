package com.gerop.stockcontrol.jewelry.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.gerop.stockcontrol.jewelry.validation.UniqueName;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="jewelry")
public class Jewel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @UniqueName
    @Size(min=3, max=30)
    private String name;

    @NotNull
    @Size(max=40)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
        name = "jewel_metal",
        joinColumns = @JoinColumn(name = "jewel_id"),
        inverseJoinColumns = @JoinColumn(name = "metal_id")
    )
    private List<Metal> metal;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="user_id")
    private User user;

    public Jewel() {
        this.metal = new ArrayList<>();
    }

    public Jewel(String name, String description) {
        this();
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Metal> getMetal() {
        return metal;
    }

    public void setMetal(List<Metal> metal) {
        this.metal = metal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
