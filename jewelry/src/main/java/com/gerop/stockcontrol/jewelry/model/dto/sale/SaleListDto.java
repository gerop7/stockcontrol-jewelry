package com.gerop.stockcontrol.jewelry.model.dto.sale;

import java.util.List;

public record SaleListDto(
    Long id,
    String description,
    Float total,
    String timestamp,
    String inventoryName,
    Long inventoryId,
    List<JewelSummaryDto> jewels
){}
