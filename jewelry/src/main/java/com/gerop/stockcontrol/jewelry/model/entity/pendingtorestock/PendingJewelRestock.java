package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import jakarta.validation.constraints.PositiveOrZero;

public class PendingJewelRestock extends PendingRestock {
    @PositiveOrZero
    private Long quantity;

    public PendingJewelRestock() {
        super();
        this.quantity = 0L;
    }

    public PendingJewelRestock(Long quantity) {
        super();
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
