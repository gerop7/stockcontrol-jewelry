package com.gerop.stockcontrol.jewelry.model.dto.sale;

public record JewelSummaryDto(
    Long id,
    String name,
    String sku,
    Long inventoryId,
    String inventoryName,
    Long quantity,
    Float total
) {}
