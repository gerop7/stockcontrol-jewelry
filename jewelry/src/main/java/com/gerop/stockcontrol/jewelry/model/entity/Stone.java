package com.gerop.stockcontrol.jewelry.model.entity;


import java.util.ArrayList;
import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingStoneRestock;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.StoneStockByInventory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="stone")
public class Stone extends Material{
    @OneToMany(mappedBy = "stone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoneStockByInventory> stockByInventory;

    @OneToMany(mappedBy = "stone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PendingStoneRestock> pendingStoneRestock;

    public Stone() {
        super();
        this.stockByInventory=new ArrayList<>();
        this.pendingStoneRestock = new ArrayList<>();
    }

    public Stone(String name, User user, boolean global) {
        super(name, user, global);
        this.stockByInventory=new ArrayList<>();
        this.pendingStoneRestock = new ArrayList<>();
    }

    public List<PendingStoneRestock> getPendingStoneRestock() {
        return pendingStoneRestock;
    }

    public void setPendingStoneRestock(List<PendingStoneRestock> pendingStoneRestock) {
        this.pendingStoneRestock = pendingStoneRestock;
    }

    public List<StoneStockByInventory> getStockByInventory() {
        return stockByInventory;
    }

    public void setStockByInventory(List<StoneStockByInventory> stockByInventory) {
        this.stockByInventory = stockByInventory;
    }


}
