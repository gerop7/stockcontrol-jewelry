package com.gerop.stockcontrol.jewelry.model.dto;

import java.util.Set;

import com.gerop.stockcontrol.jewelry.validation.UniqueSku;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record JewelDto(
    @Positive Long jewelId,
    @NotBlank String name,
    @NotBlank String description,
    @UniqueSku String sku,
    String imageUrl,
    Long categoryId,
    Long subcategoryId,
    @PositiveOrZero Float weight,
    @PositiveOrZero Float size,
    Set<Long> metalIds,
    Set<Long> stoneIds,
    Set<InventoryStockDto> stockByInventory
) {}
