package com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="stock_by_inventory")
@Getter
@Setter
@NoArgsConstructor
public abstract class StockByInventory<T extends Number> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Inventory inventory;

    @PositiveOrZero
    @NotNull
    private T stock;

    public StockByInventory(T stock, Inventory inventory) {
        this.stock = stock;
        this.inventory = inventory;
    }
}
