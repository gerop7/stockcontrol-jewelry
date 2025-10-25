package com.gerop.stockcontrol.jewelry.model.dto.sale;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CompleteSaleDto(
    @NotNull String description,
    @NotEmpty Set<JewelSaleWithPendingRestockDto> jewels,
    @NotNull Long inventoryId
){
    public CompleteSaleDto{
        if(jewels==null) jewels = new LinkedHashSet<>();
    }
}
