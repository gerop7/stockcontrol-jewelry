package com.gerop.stockcontrol.jewelry.model.dto;

import com.gerop.stockcontrol.jewelry.model.entity.enums.StockCondition;

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
        StockCondition stockCondition,
        Boolean hasPendingRestock,
        Boolean active
) {}
