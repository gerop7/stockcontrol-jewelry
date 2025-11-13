package com.gerop.stockcontrol.jewelry.model.dto.sale;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StoneQuantitySaleDto(@NotNull Long stoneId, @PositiveOrZero Long quantityToRestock, @PositiveOrZero Long quantityUsed) {
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
