package com.gerop.stockcontrol.jewelry.model.dto.materials;

import java.util.Set;

import com.gerop.stockcontrol.jewelry.model.dto.InventoryWeightDto;

public record MetalDto(
    Long id,
    String name,
    Long userId,
    boolean global,
    Set<InventoryWeightDto> stockByInventory,
    Set<InventoryWeightDto> pendingMetalRestock,
    String url
) implements MaterialDto { }
