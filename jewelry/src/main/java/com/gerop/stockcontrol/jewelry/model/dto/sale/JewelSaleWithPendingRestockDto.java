package com.gerop.stockcontrol.jewelry.model.dto.sale;

import java.util.Objects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record JewelSaleWithPendingRestockDto(
    @NotNull Long jewelId, 
    @Positive Long quantity, 
    @PositiveOrZero Long quantityToRestock,
    @PositiveOrZero Float total
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JewelSaleWithPendingRestockDto other)) return false;
        return Objects.equals(jewelId, other.jewelId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(jewelId);
    }
}
