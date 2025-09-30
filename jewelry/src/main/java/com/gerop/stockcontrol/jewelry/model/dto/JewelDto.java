package com.gerop.stockcontrol.jewelry.model.dto;

import java.util.List;

public class JewelDto {
    private String name;
    private String description;
    private Long categoryId;
    private Long subcategoryId;
    private List<Long> compositionIds;
    private Long stock;
    private String imageUrl;


    
    public JewelDto() {
    }



    public JewelDto(Long categoryId, List<Long> compositionIds, String description, String imageUrl, String name, Long stock, Long subcategoryId) {
        this.categoryId = categoryId;
        this.compositionIds = compositionIds;
        this.description = description;
        this.imageUrl = imageUrl;
        this.name = name;
        this.stock = stock;
        this.subcategoryId = subcategoryId;
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
    public List<Long> getCompositionIds() {
        return compositionIds;
    }
    public void setCompositionIds(List<Long> compositionIds) {
        this.compositionIds = compositionIds;
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

    
}
