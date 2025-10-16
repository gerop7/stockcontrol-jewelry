package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.PositiveOrZero;

public class PendingStoneRestock extends PendingRestock{
    @PositiveOrZero
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stone_id")
    private Stone stone;

    public PendingStoneRestock() {
        super();
        this.quantity = 0L;
    }
    

    public PendingStoneRestock(Inventory inventory, @PositiveOrZero Long quantity, Stone stone) {
        super(inventory);
        this.quantity = quantity;
        this.stone = stone;
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

    public Stone getStone() {
        return stone;
    }

    public void setStone(Stone stone) {
        this.stone = stone;
    }

    
}
