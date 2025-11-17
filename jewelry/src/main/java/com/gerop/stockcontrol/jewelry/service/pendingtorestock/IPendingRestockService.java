package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingRestock;

import java.util.Optional;

public interface IPendingRestockService<T extends PendingRestock, N extends Number, O> {
    T create(O entity, Inventory inventory);
    T create(O entity, Inventory inventory, N quantity);
    T createSave(O entity, Inventory inventory);
    T createSave(O entity, Inventory inventory, N quantity);
    T save(T entity);
    void addToRestock(T entity, N quantity);
    void removeFromRestock(T entity, N quantity);
    void addToRestock(O object, Inventory inventory, N quantity);
    void removeFromRestock(O object, Inventory inventory, N quantity);
    boolean existsByInventory(Long entityId, Long inventoryId);
    void remove(O object, Inventory inventory);
    Optional<T> findOne(O object, Inventory inventory);
}
