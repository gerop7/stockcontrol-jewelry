package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="metal_restock")
public class PendingMetalRestock extends PendingRestock {
    private Float weight;

    public PendingMetalRestock() {
        super();
        this.weight = 0f;
    }

    public PendingMetalRestock(Float weight) {
        super();
        this.weight =weight;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }
}
