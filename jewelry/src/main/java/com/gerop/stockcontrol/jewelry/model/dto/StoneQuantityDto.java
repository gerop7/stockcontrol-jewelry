package com.gerop.stockcontrol.jewelry.model.dto;

import jakarta.validation.constraints.Positive;

public class StoneQuantityDto {
    @Positive
    private Long stoneId;
    @Positive
    private Long quantity;

    public StoneQuantityDto(Long quantity, Long stoneId) {
        this.quantity = quantity;
        this.stoneId = stoneId;
    }

    public Long getStoneId() {
        return stoneId;
    }

    public void setStoneId(Long stoneId) {
        this.stoneId = stoneId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}
