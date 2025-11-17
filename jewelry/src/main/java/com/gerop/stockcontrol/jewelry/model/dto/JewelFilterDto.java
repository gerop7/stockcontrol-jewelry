package com.gerop.stockcontrol.jewelry.model.dto;

import java.util.Set;

public record JewelFilterDto(
        String name,
        String sku,
        Long categoryId,
        Long subcategoryId,
        Set<Long> metalIds,
        Set<Long> stoneIds,
        Float minWeight,
        Float maxWeight,
        Float minSize,
        Float maxSize,
        Float minStock,
        Float maxStock,
        Boolean hasPendingRestock,
        Boolean active
) {}
