package com.gerop.stockcontrol.jewelry.model.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="categories")
@Getter
@Setter
public class Category extends AbstractCategory {
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "inventory_categories",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "inventory_id")
    )
    private Set<Inventory> inventories = new HashSet<>();

    public Category(String name, boolean isGlobal, User owner, Set<Inventory> inventories) {
        super(name, isGlobal, owner);
        this.inventories = inventories;
    }
}
