package com.gerop.stockcontrol.jewelry.model.entity;


import java.util.ArrayList;
import java.util.List;

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
    private List<PendingStoneRestock> pendingStoneRestock;

    public Stone() {
        super();
        this.stock=0L;
        this.pendingStoneRestock = new ArrayList<>();
    }

    public Stone(List<PendingStoneRestock> pendingStoneRestock, Long stock) {
        this.pendingStoneRestock = pendingStoneRestock;
        this.stock = stock;
    }


    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public List<PendingStoneRestock> getPendingStoneRestock() {
        return pendingStoneRestock;
    }

    public void setPendingStoneRestock(List<PendingStoneRestock> pendingStoneRestock) {
        this.pendingStoneRestock = pendingStoneRestock;
    }


}
