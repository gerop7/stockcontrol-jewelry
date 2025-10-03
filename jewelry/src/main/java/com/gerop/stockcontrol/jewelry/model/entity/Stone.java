package com.gerop.stockcontrol.jewelry.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingStoneRestock;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name="stone")
public class Stone extends Material{
    @ManyToMany(mappedBy="stone")
    private List<Jewel> jewelry;

    @PositiveOrZero
    private Long stock;

    @OneToOne(mappedBy = "stone", cascade = CascadeType.ALL, orphanRemoval = true)
    private PendingStoneRestock pendingStoneRestock;

    public Stone() {
        super();
        this.jewelry = new ArrayList<>();
        this.stock=0L;
    }

    public List<Jewel> getJewelry() {
        return jewelry;
    }

    public void setJewelry(List<Jewel> jewelry) {
        this.jewelry = jewelry;
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
