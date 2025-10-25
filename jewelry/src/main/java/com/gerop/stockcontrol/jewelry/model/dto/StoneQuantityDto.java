package com.gerop.stockcontrol.jewelry.model.dto;

import jakarta.validation.constraints.Positive;

public record StoneQuantityDto(@Positive Long stoneId, @Positive Long quantity
){
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoneQuantityDto other)) return false;
        return stoneId != null && stoneId.equals(other.stoneId);
    }

    @Override
    public int hashCode() {
        return stoneId != null ? stoneId.hashCode():0;
    }
}
