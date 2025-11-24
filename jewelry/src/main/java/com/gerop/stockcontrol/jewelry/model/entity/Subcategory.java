package com.gerop.stockcontrol.jewelry.model.entity;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="subcategories")
@Getter
@Setter
@NoArgsConstructor
public class Subcategory extends AbstractCategory {
    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="principal_category_id")
    private Category principalCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "inventory_subcategories",
        joinColumns = @JoinColumn(name = "subcategory_id"),
        inverseJoinColumns = @JoinColumn(name = "inventory_id")
    )
    private Set<Inventory> inventories = new HashSet<>();

    public Subcategory(String name, boolean isGlobal, User owner) {
        super(name, isGlobal, owner);
    }

    public Subcategory(String name, boolean isGlobal, User owner, Set<Inventory> inventories, Category principalCategory) {
        super(name, isGlobal, owner);
        this.inventories = inventories;
        this.principalCategory = principalCategory;
    }
}
