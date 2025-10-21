package com.gerop.stockcontrol.jewelry.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingJewelRestock;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;
import com.gerop.stockcontrol.jewelry.validation.UniqueSku;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="jewelry",
    uniqueConstraints= @UniqueConstraint(columnNames={"sku", "user_id"}))
public class Jewel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=3, max=30)
    private String name;

    @NotNull
    @Size(max=40)
    private String description;

    @NotEmpty
    @UniqueSku(message="El código SKU ingresado es inválido.")
    private String sku;

    @OneToMany(mappedBy = "jewel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JewelryStockByInventory> stockByInventory;

    @Column(unique = true)
    private String imageUrl;

    @PositiveOrZero
    private Float weight;

    @PositiveOrZero
    private Float size;

    @OneToMany(mappedBy="jewel",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PendingJewelRestock> pendingRestock;

    
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="category_id")
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="subcategory_id")
    private Subcategory subcategory;
    
    @ManyToMany
    @JoinTable(
        name = "jewel_metal",
        joinColumns = @JoinColumn(name = "jewel_id"),
        inverseJoinColumns = @JoinColumn(name = "metal_id")
    )
    private List<Metal> metal;

    @ManyToMany
    @JoinTable(
        name = "jewel_stone",
        joinColumns = @JoinColumn(name = "jewel_id"),
        inverseJoinColumns = @JoinColumn(name = "stone_id")
    )
    private List<Stone> stone;
        
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "inventory_jewel",
        joinColumns = @JoinColumn(name = "jewel_id"),
        inverseJoinColumns = @JoinColumn(name = "inventory_id")
    )
    private List<Inventory> inventories;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="user_id")
    private User user;

    private boolean active = true;
    
    public Jewel() {
        this.metal = new ArrayList<>();
        this.stone = new ArrayList<>();
        this.inventories=new ArrayList<>();
        this.pendingRestock = new ArrayList<>();
        this.stockByInventory = new ArrayList<>();
        this.weight=0f;
        this.size=0f;
    }
    
    public Jewel(String name,String description, String sku, String imageUrl,
        Float weight, Float size, User user) {
        this.metal = new ArrayList<>();
        this.stone = new ArrayList<>();
        this.inventories=new ArrayList<>();
        this.stockByInventory = new ArrayList<>();
        this.pendingRestock = new ArrayList<>();
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.imageUrl = imageUrl;
        this.weight = weight;
        this.size = size;
        this.user=user;
    }

    public Jewel(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
        
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category2) {
        this.category = category2;
    }
    
    public List<Metal> getMetal() {
        return metal;
    }
    
    public void setMetal(List<Metal> metal) {
        this.metal = metal;
    }
    
    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Stone> getStone() {
        return stone;
    }

    public void setStone(List<Stone> stone) {
        this.stone = stone;
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


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    public List<JewelryStockByInventory> getStockByInventory() {
        return stockByInventory;
    }

    public void setStockByInventory(List<JewelryStockByInventory> stockByInventory) {
        this.stockByInventory = stockByInventory;
    }

    public List<PendingJewelRestock> getPendingRestock() {
        return pendingRestock;
    }

    public void setPendingRestock(List<PendingJewelRestock> pendingRestock) {
        this.pendingRestock = pendingRestock;
    }
}    
