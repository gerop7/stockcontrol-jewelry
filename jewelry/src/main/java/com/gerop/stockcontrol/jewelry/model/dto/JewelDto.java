package com.gerop.stockcontrol.jewelry.model.dto;

import java.util.List;

import com.gerop.stockcontrol.jewelry.validation.UniqueSku;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class JewelDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @UniqueSku
    private String sku;
    private Long categoryId;
    private Long subcategoryId;
    private List<Long> metalIds;
    private List<Long> stoneIds;
    @PositiveOrZero
    private Long stock;
    private String imageUrl;
    @PositiveOrZero
    private Float weight;
    @PositiveOrZero
    private Float size;
    
    public JewelDto() {
    }

    public JewelDto(Long categoryId, List<Long> metalIds, String description, String imageUrl, String name, Long stock, Long subcategoryId, Float weight, Float size,String sku,List<Long> stoneIds) {
        this.categoryId = categoryId;
        this.metalIds = metalIds;
        this.description = description;
        this.imageUrl = imageUrl;
        this.name = name;
        this.stock = stock;
        this.subcategoryId = subcategoryId;
        this.size=size;
        this.weight=weight;
        this.sku=sku;
        this.stoneIds=stoneIds;
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
    public Long getStock() {
        return stock;
    }
    public void setStock(Long stock) {
        this.stock = stock;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<Long> getStoneIds() {
        return stoneIds;
    }

    public void setStoneIds(List<Long> stoneIds) {
        this.stoneIds = stoneIds;
    }

    
}
