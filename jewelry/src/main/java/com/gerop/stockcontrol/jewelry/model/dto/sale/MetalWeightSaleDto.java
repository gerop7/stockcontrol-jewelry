package com.gerop.stockcontrol.jewelry.model.dto.sale;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MetalWeightSaleDto(@NotNull Long metalId, @Positive Float weightToRestock, @Positive Float weightUsed, @Positive Float weightReceived)  {
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
