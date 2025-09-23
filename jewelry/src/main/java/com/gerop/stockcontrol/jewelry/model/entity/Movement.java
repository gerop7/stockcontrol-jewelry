package com.gerop.stockcontrol.jewelry.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name="movements")
public class Movement {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String type;
    @NotBlank
    private String description;
    @Positive
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="jewel_id")
    private Jewel jewel;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="user_id")
    private User user;

    private LocalDateTime timestamp;
    
    public Movement() {
        this.timestamp= LocalDateTime.now();
    }

    public Movement(String type, String description, Long quantity) {
        this();
        this.type = type;
        this.description = description;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Jewel getJewel() {
        return jewel;
    }

    public void setJewel(Jewel jewel) {
        this.jewel = jewel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
