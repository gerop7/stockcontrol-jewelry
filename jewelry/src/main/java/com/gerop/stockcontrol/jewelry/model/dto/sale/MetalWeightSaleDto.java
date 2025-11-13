package com.gerop.stockcontrol.jewelry.model.dto.sale;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record MetalWeightSaleDto(@NotNull Long metalId, @PositiveOrZero Float weightToRestock, @PositiveOrZero Float weightUsed)  {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetalWeightSaleDto other)) return false;
        return metalId != null && metalId.equals(other.metalId);
    }

    @Override
    public int hashCode() {
        return metalId!=null?metalId.hashCode():0;
    }
}
