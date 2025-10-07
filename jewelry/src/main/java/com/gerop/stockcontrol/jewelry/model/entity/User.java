package com.gerop.stockcontrol.jewelry.model.entity;

import com.gerop.stockcontrol.jewelry.validation.UniqueUsername;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @UniqueUsername
    @Size(min=3, max=30)
    private String username;
    
    @NotBlank
    @Size(min=8, max=100)
    private String password;
    
    public User() {
    }
    
    public User(String password, String username) {
        this.password = password;
        this.username = username;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}