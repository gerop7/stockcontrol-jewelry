package com.gerop.stockcontrol.jewelry.model.dto;

import jakarta.validation.constraints.Positive;

public record MetalWeightDto(@Positive Long metalId, @Positive Float weight
){
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetalWeightDto other)) return false;
        return metalId != null && metalId.equals(other.metalId);
    }

    @Override
    public int hashCode() {
        return metalId!=null?metalId.hashCode():0;
    }
}
