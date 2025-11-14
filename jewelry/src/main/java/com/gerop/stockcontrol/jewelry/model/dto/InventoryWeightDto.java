package com.gerop.stockcontrol.jewelry.model.dto;

import java.util.Objects;

public record InventoryWeightDto(Long inventoryId, Float weight) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof InventoryWeightDto other)) return false;
        return Objects.equals(inventoryId, other.inventoryId)
            && Objects.equals(weight, other.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryId, weight);
    }
}
