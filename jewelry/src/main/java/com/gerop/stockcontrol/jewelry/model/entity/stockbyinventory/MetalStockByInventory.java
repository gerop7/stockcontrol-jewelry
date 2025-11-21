package com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Setter
@Getter
@Entity
@Table(
    name = "metal_stock_by_inventory",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"inventory_id", "metal_id"})
    }
)
@NoArgsConstructor
public class MetalStockByInventory extends StockByInventory<Float>{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metal_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Metal metal;

    public MetalStockByInventory(Float stock, Inventory inventory, Metal metal) {
        super(stock, inventory);
        this.metal = metal;
    }
}
