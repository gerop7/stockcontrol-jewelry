package com.gerop.stockcontrol.jewelry.service.stockperinventory;

import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.StockByInventory;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;

import java.util.Optional;

public abstract class AbstractStockByInventoryService<T extends StockByInventory<N>, O, N extends Number> implements IStockByInventoryService<T, O, N> {
    protected abstract T getStockOrThrow(O object, Inventory inventory);
    protected abstract T getStockOrCreate(O object, Inventory inventory);
    protected abstract T save(T stock);
    protected abstract T newStock(O object, Inventory inventory, N quantity);
    protected abstract String getObjectName(O object);
    protected abstract void applyAddition(T stock, N quantity);
    protected abstract void applySubtraction(T stock, N quantity);
    public abstract Optional<T> findOne(Long objId, Long inventoryId);


    @Override
    @Transactional
    public T create(O object, Inventory inventory, N quantity) {
        validateParams(object, inventory, quantity);
        T stock = newStock(object, inventory, quantity);
        return save(stock);
    }
    
    @Override
    @Transactional
    public void addStock(O object, Inventory inventory, N quantity) {
        validateParams(object, inventory, quantity);
        T stock = getStockOrCreate(object, inventory);
        applyAddition(stock, quantity);
        save(stock);
    }

    @Override
    @Transactional
    public void removeStock(O object, Inventory inventory, N quantity) {
        validateParams(object, inventory, quantity);
        T stock = getStockOrThrow(object, inventory);
        applySubtraction(stock, quantity);
        save(stock);
    }


    @Override
    public Optional<T> findOne(O object, Inventory inventory) {
        return  Optional.of(getStockOrThrow(object, inventory));
    }

    @Override
    public void remove(T stock) {
        throw new UnsupportedOperationException("Debe implementarse en la subclase si es necesario.");
    }

    private void validateParams(O object, Inventory inventory, N quantity) {
        if (object == null || inventory == null)
            throw new RequiredFieldException("Debes completar todos los campos!");
        if (quantity == null || quantity.doubleValue() < 0)
            throw new InvalidQuantityException("Cantidad inválida para " + getObjectName(object));
    }
}
