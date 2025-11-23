package com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;

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
    name = "jewelry_stock_by_inventory",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"inventory_id", "jewel_id"})
    })
@NoArgsConstructor
public class JewelryStockByInventory extends StockByInventory<Long>{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jewel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Jewel jewel;

    public JewelryStockByInventory(Inventory inventory,  Jewel jewel, Long stock) {
        super(stock, inventory);
        this.jewel = jewel;
    }
}
