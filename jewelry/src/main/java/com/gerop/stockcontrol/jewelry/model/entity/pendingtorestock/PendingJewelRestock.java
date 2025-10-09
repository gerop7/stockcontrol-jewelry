package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name="jewel_restock")
public class PendingJewelRestock extends PendingRestock {
    @PositiveOrZero
    private Long quantity;

    public PendingJewelRestock() {
        super();
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
