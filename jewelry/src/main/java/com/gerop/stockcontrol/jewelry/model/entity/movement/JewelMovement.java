package com.gerop.stockcontrol.jewelry.model.entity.movement;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.enums.JewelMovementType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name="jewel_movements")
public class JewelMovement extends Movement{
    @Enumerated(EnumType.STRING)
    @NotNull
    private JewelMovementType type;
    @PositiveOrZero
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="jewel_id")
    @NotNull
    private Jewel jewel;

    public JewelMovement() {
        super();
    }

    public JewelMovement(Long quantity) {
        this();
        this.quantity = quantity;
    }


    public JewelMovementType getType() {
        return type;
    }

    public void setType(JewelMovementType type) {
        this.type = type;
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
