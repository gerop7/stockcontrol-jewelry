package com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(
    name = "metal_stock_by_inventory",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"inventory_id", "metal_id"})
    }
)
public class MetalStockByInventory {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metal_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Metal metal;

    @PositiveOrZero
    @NotNull
    private Float stock;

    public MetalStockByInventory() {
        this.stock = 0f;
    }

    public MetalStockByInventory(Inventory inventory, Metal metal, Float stock) {
        this.inventory = inventory;
        this.metal = metal;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Metal getMetal() {
        return metal;
    }

    public void setMetal(Metal metal) {
        this.metal = metal;
    }

    public Float getStock() {
        return stock;
    }

    public void setStock(Float stock) {
        this.stock = stock;
    }


}
