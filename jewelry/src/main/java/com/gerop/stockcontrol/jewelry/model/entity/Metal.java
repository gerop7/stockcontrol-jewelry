package com.gerop.stockcontrol.jewelry.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingMetalRestock;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.MetalStockByInventory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

    
    public Metal(String name, User user, boolean global, String urlImage) {
        super(name, user, global, urlImage);
        this.stockByInventory=new ArrayList<>();
        this.pendingMetalRestock = new ArrayList<>();
    }


}
