package com.gerop.stockcontrol.jewelry.model.dto.sale;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StoneQuantitySaleDto(@NotNull Long stoneId, @Positive Long quantityToRestock, @Positive Long quantityUsed, @Positive Long quantityReceived) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoneQuantitySaleDto other)) return false;
        return stoneId != null && stoneId.equals(other.stoneId);
    }

    @Override
    public int hashCode() {
        return stoneId != null ? stoneId.hashCode():0;
    }
}
