package com.gerop.stockcontrol.jewelry.model.entity;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingMetalRestock;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name="metal")
public class Metal extends Material {
    @PositiveOrZero
    private Float weight;

    @OneToOne(mappedBy = "metal", cascade = CascadeType.ALL, orphanRemoval = true)
    private PendingMetalRestock pendingMetalRestock;

    public Metal() {
        super();
        this.weight=0f;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }


    public PendingMetalRestock getPendingMetalRestock() {
        return pendingMetalRestock;
    }

    public void setPendingMetalRestock(PendingMetalRestock pendingMetalRestock) {
        this.pendingMetalRestock = pendingMetalRestock;
    }
}
