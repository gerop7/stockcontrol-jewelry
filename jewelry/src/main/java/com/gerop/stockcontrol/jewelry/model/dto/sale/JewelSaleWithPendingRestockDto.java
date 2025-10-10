package com.gerop.stockcontrol.jewelry.model.dto.sale;

import java.util.ArrayList;
import java.util.List;

import com.gerop.stockcontrol.jewelry.model.dto.MetalWeightDto;
import com.gerop.stockcontrol.jewelry.model.dto.StoneQuantityDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class JewelSaleWithPendingRestockDto {
    @NotNull
    private Long jewelId;

    @Positive
    private Long quantity;

    @PositiveOrZero
    private Long quantityToRestock;

    @PositiveOrZero
    private Float total;

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

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }
}
