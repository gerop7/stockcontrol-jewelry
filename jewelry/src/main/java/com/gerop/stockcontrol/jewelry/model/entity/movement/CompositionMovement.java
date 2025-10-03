package com.gerop.stockcontrol.jewelry.model.entity.movement;

import com.gerop.stockcontrol.jewelry.model.entity.Composition;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
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
@Table(name="composition_movements")
public class CompositionMovement extends Movement {
    @Enumerated(EnumType.STRING)
    @NotNull
    private CompositionMovementType type;

    @PositiveOrZero
    private Float weight;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="composition_id")
    @NotNull
    private Composition composition;

    @ManyToOne
    @JoinColumn(name="jewel_id")
    private Jewel jewel;

    public CompositionMovement(){
        super();
    }

    public CompositionMovement(Float weight){
        this();
        this.weight=weight;
    }

    public CompositionMovementType getType() {
        return type;
    }

    public void setType(CompositionMovementType type) {
        this.type = type;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Composition getComposition() {
        return composition;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    public Jewel getJewel() {
        return jewel;
    }

    public void setJewel(Jewel jewel) {
        this.jewel = jewel;
    }

    
}
