package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import jakarta.validation.constraints.PositiveOrZero;

public class PendingStoneRestock extends PendingRestock{
    @PositiveOrZero
    private Long quantity;


    public PendingStoneRestock() {
        super();
        this.quantity = 0L;
    }
    

    public PendingStoneRestock(Long quantity) {
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
