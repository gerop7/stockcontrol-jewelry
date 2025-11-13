package com.gerop.stockcontrol.jewelry.model.dto.sale;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record JewelSaleWithPendingRestockDto(
    @NotNull Long jewelId, 
    @Positive Long quantity, 
    @PositiveOrZero Long quantityToRestock,
    @PositiveOrZero Float total,
    Set<MetalWeightSaleDto> metalToRestock,
    Set<StoneQuantitySaleDto> stoneToRestock
) {
    public JewelSaleWithPendingRestockDto {
        if (metalToRestock==null) metalToRestock = new LinkedHashSet<>();
        if (stoneToRestock==null) stoneToRestock = new LinkedHashSet<>();
    }

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
