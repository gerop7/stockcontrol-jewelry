package com.gerop.stockcontrol.jewelry.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingMetalRestock;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name="metal")
public class Metal extends Material {
    @ManyToMany(mappedBy="metal")
    private List<Jewel> jewelry;

    @PositiveOrZero
    private Float weight;

    @OneToOne(mappedBy = "metal", cascade = CascadeType.ALL, orphanRemoval = true)
    private PendingMetalRestock pendingMetalRestock;

    public Metal() {
        super();
        this.jewelry = new ArrayList<>();
        this.weight=0f;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }


    public List<Jewel> getJewelry() {
        return jewelry;
    }

    public void setJewelry(List<Jewel> jewelry) {
        this.jewelry = jewelry;
    }

    public PendingMetalRestock getPendingMetalRestock() {
        return pendingMetalRestock;
    }

    public void setPendingMetalRestock(PendingMetalRestock pendingMetalRestock) {
        this.pendingMetalRestock = pendingMetalRestock;
    }
}
