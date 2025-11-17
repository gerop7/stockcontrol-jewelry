package com.gerop.stockcontrol.jewelry.service.stockperinventory;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;

import java.util.Optional;

public interface IStockByInventoryService<T, O, N extends Number> {
    T create(O object, Inventory inventory, N quantity);
    void remove(T stock);
    void addStock(O object, Inventory inventory, N quantity);
    void removeStock(O object, Inventory inventory, N quantity);

    Optional<T> findOne(O object, Inventory inventory);
}
