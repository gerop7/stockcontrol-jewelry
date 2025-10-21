package com.gerop.stockcontrol.jewelry.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="subcategories",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name"}))
public class Subcategory {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=3, max=30)
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="principal_category_id")
    private Category principalCategory;

    private boolean global;

    public Subcategory() {
        this.global=false;
    }


    public Subcategory(String name, User user,
            Category principalCategory, boolean global) {
        this.name = name;
        this.user = user;
        this.principalCategory = principalCategory;
        this.global = global;
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

    public Category getPrincipalCategory() {
        return principalCategory;
    }

    public void setPrincipalCategory(Category principalCategory) {
        this.principalCategory = principalCategory;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public boolean isGlobal() {
        return global;
    }


    public void setGlobal(boolean global) {
        this.global = global;
    }

    
}
