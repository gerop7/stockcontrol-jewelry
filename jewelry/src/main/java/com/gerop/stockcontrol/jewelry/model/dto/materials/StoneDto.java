package com.gerop.stockcontrol.jewelry.model.dto.materials;

import java.util.Set;

import com.gerop.stockcontrol.jewelry.model.dto.InventoryStockDto;

public record StoneDto(
    Long id,
    String name,
    Long userId,
    boolean global,
    Set<InventoryStockDto> stockByInventory,
    Set<InventoryStockDto> pendingRestock,
    String url
) implements MaterialDto { }
