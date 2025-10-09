package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="metal_restock")
public class PendingMetalRestock extends PendingRestock {
    private Float weight;

    public PendingMetalRestock() {
        super();
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }
}
