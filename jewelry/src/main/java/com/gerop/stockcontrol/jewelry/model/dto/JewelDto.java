package com.gerop.stockcontrol.jewelry.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.gerop.stockcontrol.jewelry.validation.UniqueSku;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class JewelDto {
    @Positive
    private Long jewelId;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @UniqueSku
    private String sku;
    @PositiveOrZero
    private String imageUrl;

    private Long categoryId;
    private Long subcategoryId;
    private List<Long> metalIds;
    private List<Long> stoneIds;
    private List<InventoryStockDto> stockByInventory;

    @PositiveOrZero
    private Float weight;
    @PositiveOrZero
    private Float size;

    public JewelDto() {
        metalIds = new ArrayList<>();
        stoneIds = new ArrayList<>();
        stockByInventory = new ArrayList<>();
    }

    public JewelDto(Long jewelId,String name, String description, String sku,
            String imageUrl, Long categoryId, Long subcategoryId, List<Long> metalIds,
            List<Long> stoneIds, List<InventoryStockDto> stockByInventory, Float weight,
            Float size) {
        this.jewelId = jewelId;
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.metalIds = metalIds;
        this.stoneIds = stoneIds;
        this.stockByInventory = stockByInventory;
        this.weight = weight;
        this.size = size;
    }



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public Long getSubcategoryId() {
        return subcategoryId;
    }
    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }
    public List<Long> getMetalIds() {
        return metalIds;
    }
    public void setMetalIds(List<Long> metalIds) {
        this.metalIds = metalIds;
    }
    public List<Long> getStoneIds() {
        return stoneIds;
    }
    public void setStoneIds(List<Long> stoneIds) {
        this.stoneIds = stoneIds;
    }
    public List<InventoryStockDto> getStockByInventory() {
        return stockByInventory;
    }
    public void setStockByInventory(List<InventoryStockDto> stockByInventory) {
        this.stockByInventory = stockByInventory;
    }
    public Float getWeight() {
        return weight;
    }
    public void setWeight(Float weight) {
        this.weight = weight;
    }
    public Float getSize() {
        return size;
    }
    public void setSize(Float size) {
        this.size = size;
    }



    public Long getJewelId() {
        return jewelId;
    }



    public void setJewelId(Long jewelId) {
        this.jewelId = jewelId;
    }


}
