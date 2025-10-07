package com.gerop.stockcontrol.jewelry.model.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class CompleteSaleDto {
    @NotNull
    private String description;
    @PositiveOrZero
    private Float total;
    @NotEmpty
    private List<JewelSaleWithPendingRestockDto> jewels;

    public CompleteSaleDto() {
        jewels= new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public List<JewelSaleWithPendingRestockDto> getJewels() {
        return jewels;
    }

    public void setJewels(List<JewelSaleWithPendingRestockDto> jewels) {
        this.jewels = jewels;
    }


}
