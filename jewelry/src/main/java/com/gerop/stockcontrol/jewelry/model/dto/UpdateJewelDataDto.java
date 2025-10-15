package com.gerop.stockcontrol.jewelry.model.dto;

public class UpdateJewelDataDto {
    private String name;
    private String description;
    private String imageUrl;
    private String sku;
    private Float size;
    private Float weight;
    public UpdateJewelDataDto(String name, String description, String imageUrl, String sku, Float size, Float weight) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.sku = sku;
        this.size = size;
        this.weight = weight;
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
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    public Float getSize() {
        return size;
    }
    public void setSize(Float size) {
        this.size = size;
    }
    public Float getWeight() {
        return weight;
    }
    public void setWeight(Float weight) {
        this.weight = weight;
    }


}
