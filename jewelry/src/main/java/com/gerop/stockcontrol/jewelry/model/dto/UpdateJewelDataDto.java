package com.gerop.stockcontrol.jewelry.model.dto;

public record UpdateJewelDataDto(
    String name,
    String description,
    String imageUrl,
    String sku,
    Float size,
    Float weight
) {}
