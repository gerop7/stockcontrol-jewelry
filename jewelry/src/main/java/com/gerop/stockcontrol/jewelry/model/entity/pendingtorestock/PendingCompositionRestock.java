package com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock;

import com.gerop.stockcontrol.jewelry.model.entity.Composition;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="composition_restock")
public class PendingCompositionRestock extends PendingRestock {
    @OneToOne
    @JoinColumn(name = "composition_id", unique = true)
    private Composition composition;

    private Float weight;

    public PendingCompositionRestock() {
        super();
    }

    public Composition getComposition() {
        return composition;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }
}
