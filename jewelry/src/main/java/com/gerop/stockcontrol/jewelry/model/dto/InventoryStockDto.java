package com.gerop.stockcontrol.jewelry.model.dto;

public class InventoryStockDto {
    private Long inventoryId;
    private Long stock;

    public InventoryStockDto() {
    }
    
    public InventoryStockDto(Long inventoryId, Long stock) {
        this.inventoryId = inventoryId;
        this.stock = stock;
    }

    public Long getInventoryId() {
        return inventoryId;
    }
    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }
    public Long getStock() {
        return stock;
    }
    public void setStock(Long stock) {
        this.stock = stock;
    }

    
}
