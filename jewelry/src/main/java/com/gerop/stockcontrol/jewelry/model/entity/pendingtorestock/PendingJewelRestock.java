package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(
    name = "pending_jewel_restock",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"inventory_id", "jewel_id"})
    }
)
public class PendingJewelRestock extends PendingRestock {
    @PositiveOrZero
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "jewel_id")
    private Jewel jewel;

    public PendingJewelRestock() {
        super();
        this.quantity = 0L;
    }

    public PendingJewelRestock(Jewel jewel, Long quantity, Inventory inventory) {
        super(inventory);
        this.jewel = jewel;
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Jewel getJewel() {
        return jewel;
    }

    public void setJewel(Jewel jewel) {
        this.jewel = jewel;
    }
}
