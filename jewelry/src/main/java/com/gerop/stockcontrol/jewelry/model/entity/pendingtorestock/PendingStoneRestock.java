package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name="stone_restock")
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
