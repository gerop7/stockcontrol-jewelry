package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(
    name = "pending_metal_restock",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"inventory_id", "metal_id"})
    }
)
public class PendingMetalRestock extends PendingRestock {
    @PositiveOrZero
    private Float weight;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "metal_id")
    private Metal metal;

    public PendingMetalRestock() {
        super();
        this.weight = 0f;
    }

    public PendingMetalRestock(Metal metal, Float weight, Inventory inventory) {
        super(inventory);
        this.metal = metal;
        this.weight = weight;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Metal getMetal() {
        return metal;
    }

    public void setMetal(Metal metal) {
        this.metal = metal;
    }
}
