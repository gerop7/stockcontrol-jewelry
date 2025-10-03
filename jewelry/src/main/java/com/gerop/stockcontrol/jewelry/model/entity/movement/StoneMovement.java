package com.gerop.stockcontrol.jewelry.model.entity.movement;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;

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
@Table(name="stone_movements")
public class StoneMovement extends Movement{
    @Enumerated(EnumType.STRING)
    @NotNull
    private CompositionMovementType type;

    @PositiveOrZero
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="stone_id")
    @NotNull
    private Stone stone;

    @ManyToOne
    @JoinColumn(name="jewel_id")
    private Jewel jewel;

    public StoneMovement(){
        super();
    }

    public StoneMovement(Long quantity){
        this();
        this.quantity=quantity;
    }

    public CompositionMovementType getType() {
        return type;
    }

    public void setType(CompositionMovementType type) {
        this.type = type;
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

    public Jewel getJewel() {
        return jewel;
    }

    public void setJewel(Jewel jewel) {
        this.jewel = jewel;
    }
}
