package com.gerop.stockcontrol.jewelry.model.dto;

import java.util.List;

public class JewelDto {
    private String name;
    private String description;
    private Long categoryId;
    private Long subcategoryId;
    private List<Long> metalIds;
    private Long stock;
    private String imageUrl;
    private Float weight;
    private Float size;
    
    public JewelDto() {
    }

    public JewelDto(Long categoryId, List<Long> metalIds, String description, String imageUrl, String name, Long stock, Long subcategoryId, Float weight, Float size) {
        this.categoryId = categoryId;
        this.metalIds = metalIds;
        this.description = description;
        this.imageUrl = imageUrl;
        this.name = name;
        this.stock = stock;
        this.subcategoryId = subcategoryId;
        this.size=size;
        this.weight=weight;
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

    
}
