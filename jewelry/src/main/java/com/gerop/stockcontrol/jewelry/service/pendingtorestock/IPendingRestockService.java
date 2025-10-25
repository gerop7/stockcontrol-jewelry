package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingRestock;

public interface IPendingRestockService<T extends PendingRestock, N extends Number, O extends Object> {
    T create(O entity, Inventory inventory);
    T create(O entity, Inventory inventory, N quantity);
    T createSave(O entity, Inventory inventory);
    T createSave(O entity, Inventory inventory, N quantity);
    T save(T entity);
    void addToRestock(T entity, N quantity);
    void removeFromRestock(T entity, N quantity);
    void addToRestock(Long entityId, Long inventoryId, N quantity);
    void removeFromRestock(Long entityId, Long inventoryId, N quantity);
    boolean existsByInventory(Long entityId, Long inventoryId);
}
