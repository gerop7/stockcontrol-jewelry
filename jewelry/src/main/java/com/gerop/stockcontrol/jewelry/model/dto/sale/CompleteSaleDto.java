package com.gerop.stockcontrol.jewelry.model.dto.sale;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CompleteSaleDto {
    @NotNull
    private String description;
    @NotEmpty
    private List<JewelSaleWithPendingRestockDto> jewels;
    @NotNull
    private Long inventoryId;


    public CompleteSaleDto(String description, List<JewelSaleWithPendingRestockDto> jewels,
            Long inventoryId) {
        this.description = description;
        this.jewels = jewels;
        this.inventoryId = inventoryId;
    }

    public CompleteSaleDto() {
        jewels= new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<JewelSaleWithPendingRestockDto> getJewels() {
        return jewels;
    }

    public void setJewels(List<JewelSaleWithPendingRestockDto> jewels) {
        this.jewels = jewels;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }


}
