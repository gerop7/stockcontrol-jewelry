package com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;

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
    name = "stone_stock_by_inventory",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"inventory_id", "stone_id"})
    }
)
public class StoneStockByInventory {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stone_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stone stone;

    @PositiveOrZero
    @NotNull
    private Long stock;

    public StoneStockByInventory() {
        this.stock = 0L;
    }

    public StoneStockByInventory(Inventory inventory, Long stock, Stone stone) {
        this.inventory = inventory;
        this.stock = stock;
        this.stone = stone;
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

    public Stone getStone() {
        return stone;
    }

    public void setStone(Stone stone) {
        this.stone = stone;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }
}
