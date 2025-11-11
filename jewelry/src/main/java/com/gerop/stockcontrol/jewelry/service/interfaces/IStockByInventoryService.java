package com.gerop.stockcontrol.jewelry.service.interfaces;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;

public interface IStockByInventoryService<T extends Object,O extends Object, N extends Number> {
    T create(O object, Inventory inventory, N quantity);
    void remove(T stock);
    T addStock(O object, Inventory inventory, N quantity);
    T removeStock(O object, Inventory inventory, N quantity);

    T findOne(O object, Inventory inventory);
}
