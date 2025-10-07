package com.gerop.stockcontrol.jewelry.model.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public class JewelSaleWithPendingRestockDto {
    @NotNull
    private Long jewelId;

    private Long quantity;

    private Long quantityToRestock;

    private List<MetalWeightDto> metalToRestock;

    private List<StoneQuantityDto> stoneToRestock;

    public JewelSaleWithPendingRestockDto() {
        metalToRestock=new ArrayList<>();
        stoneToRestock=new ArrayList<>();
    }

    public Long getJewelId() {
        return jewelId;
    }

    public void setJewelId(Long jewelId) {
        this.jewelId = jewelId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantityToRestock() {
        return quantityToRestock;
    }

    public void setQuantityToRestock(Long quantityToRestock) {
        this.quantityToRestock = quantityToRestock;
    }

    public List<MetalWeightDto> getMetalToRestock() {
        return metalToRestock;
    }

    public void setMetalToRestock(List<MetalWeightDto> metalToRestock) {
        this.metalToRestock = metalToRestock;
    }

    public List<StoneQuantityDto> getStoneToRestock() {
        return stoneToRestock;
    }

    public void setStoneToRestock(List<StoneQuantityDto> stoneToRestock) {
        this.stoneToRestock = stoneToRestock;
    }
}
