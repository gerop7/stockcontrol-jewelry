package com.gerop.stockcontrol.jewelry.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name="sales")
public class Sale {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero
    private Float total;

    @OneToMany
    @JoinColumn(name="sale_id")
    private List<Jewel> jewels;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="user_id")
    private User user;

    public Sale() {
        jewels = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public List<Jewel> getJewels() {
        return jewels;
    }

    public void setJewels(List<Jewel> jewels) {
        this.jewels = jewels;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
