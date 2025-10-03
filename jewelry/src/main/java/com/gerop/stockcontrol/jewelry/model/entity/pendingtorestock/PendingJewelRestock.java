package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name="jewel_restock")
public class PendingJewelRestock extends PendingRestock {

    @OneToOne
    @JoinColumn(name = "jewel_id", unique = true)
    private Jewel jewel;

    @PositiveOrZero
    private Long quantity;

    public PendingJewelRestock() {
        super();
    }

    public Jewel getJewel() {
        return jewel;
    }

    public void setJewel(Jewel jewel) {
        this.jewel = jewel;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
