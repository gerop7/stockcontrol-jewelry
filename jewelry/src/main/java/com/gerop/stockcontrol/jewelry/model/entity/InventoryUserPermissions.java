package com.gerop.stockcontrol.jewelry.model.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryPermissionsStatus;
import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryUserPermissionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
    name="inventory_user_permissions",
    uniqueConstraints = @UniqueConstraint(columnNames = {"inventory_id", "user_id", "type"})
)
public class InventoryUserPermissions {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Inventory inventory;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Enumerated(EnumType.STRING)
    @NotNull
    private InventoryUserPermissionType type;

    @Enumerated(EnumType.STRING)
    @NotNull
    private InventoryPermissionsStatus status;

    public InventoryUserPermissions() {
    }

    public InventoryUserPermissions(Inventory inventory, InventoryPermissionsStatus status, InventoryUserPermissionType type, User user) {
        this.inventory = inventory;
        this.status = status;
        this.type = type;
        this.user = user;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    

    public InventoryPermissionsStatus getStatus() {
        return status;
    }

    public void setStatus(InventoryPermissionsStatus status) {
        this.status = status;
    }

    public InventoryUserPermissionType getType() {
        return type;
    }

    public void setType(InventoryUserPermissionType type) {
        this.type = type;
    }


}
