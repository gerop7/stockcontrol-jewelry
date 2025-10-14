package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import jakarta.validation.constraints.PositiveOrZero;

public class PendingMetalRestock extends PendingRestock {
    @PositiveOrZero
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
