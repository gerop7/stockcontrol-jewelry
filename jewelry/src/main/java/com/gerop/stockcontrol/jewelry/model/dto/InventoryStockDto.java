package com.gerop.stockcontrol.jewelry.model.dto;

import java.util.Objects;

public record InventoryStockDto(
    Long inventoryId,
    Long stock
) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof InventoryStockDto other)) return false;
        return Objects.equals(inventoryId, other.inventoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryId, stock);
    }
}
