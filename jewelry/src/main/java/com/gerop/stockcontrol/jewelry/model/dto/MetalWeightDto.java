package com.gerop.stockcontrol.jewelry.model.dto;

import jakarta.validation.constraints.Positive;

public class MetalWeightDto {
    @Positive
    private Long metalId;
    @Positive
    private Float weight;

    public MetalWeightDto(Long metalId, Float weight) {
        this.metalId = metalId;
        this.weight = weight;
    }
    public Long getMetalId() {
        return metalId;
    }
    public void setMetalId(Long metalId) {
        this.metalId = metalId;
    }
    public Float getWeight() {
        return weight;
    }
    public void setWeight(Float weight) {
        this.weight = weight;
    }
}
