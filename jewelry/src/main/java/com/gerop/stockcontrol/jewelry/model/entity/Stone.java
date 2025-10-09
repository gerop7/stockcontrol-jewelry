package com.gerop.stockcontrol.jewelry.model.entity;


import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingStoneRestock;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name="stone")
public class Stone extends Material{
    @PositiveOrZero
    private Long stock;

    @OneToOne(mappedBy = "stone", cascade = CascadeType.ALL, orphanRemoval = true)
    private PendingStoneRestock pendingStoneRestock;

    public Stone() {
        super();
        this.stock=0L;
    }


    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public PendingStoneRestock getPendingStoneRestock() {
        return pendingStoneRestock;
    }

    public void setPendingStoneRestock(PendingStoneRestock pendingStoneRestock) {
        this.pendingStoneRestock = pendingStoneRestock;
    }
}
