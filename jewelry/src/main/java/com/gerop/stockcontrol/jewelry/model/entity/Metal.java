package com.gerop.stockcontrol.jewelry.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingMetalRestock;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.MetalStockByInventory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="metal")
public class Metal extends Material {
    @OneToMany(mappedBy = "metal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetalStockByInventory> stockByInventory;

    @OneToMany(mappedBy = "metal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PendingMetalRestock> pendingMetalRestock;

    public Metal() {
        super();
        this.stockByInventory=new ArrayList<>();
        this.pendingMetalRestock = new ArrayList<>();
    }

    

    public List<MetalStockByInventory> getStockByInventory() {
        return stockByInventory;
    }

    public void setStockByInventory(List<MetalStockByInventory> stockByInventory) {
        this.stockByInventory = stockByInventory;
    }

    public List<PendingMetalRestock> getPendingMetalRestock() {
        return pendingMetalRestock;
    }

    public void setPendingMetalRestock(List<PendingMetalRestock> pendingMetalRestock) {
        this.pendingMetalRestock = pendingMetalRestock;
    }
}
