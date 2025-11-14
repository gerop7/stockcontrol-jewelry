package com.gerop.stockcontrol.jewelry.service.stockperinventory;

import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;

public abstract class AbstractStockByInventoryService<T, O, N extends Number > implements IStockByInventoryService<T, O, N> {
    protected abstract T getStockOrThrow(O object, Inventory inventory);
    protected abstract T save(T stock);
    protected abstract T newStock(O object, Inventory inventory, N quantity);
    protected abstract String getObjectName(O object);
    protected abstract void applyAddition(T stock, N quantity);
    protected abstract void applySubtraction(T stock, N quantity);
    
    @Override
    @Transactional
    public T addStock(O object, Inventory inventory, N quantity) {
        validateParams(object, inventory, quantity);
        T stock = getStockOrThrow(object, inventory);
        applyAddition(stock, quantity);
        return save(stock);
    }

    @Override
    @Transactional
    public T removeStock(O object, Inventory inventory, N quantity) {
        validateParams(object, inventory, quantity);
        T stock = getStockOrThrow(object, inventory);
        applySubtraction(stock, quantity);
        return save(stock);
    }

    @Override
    @Transactional
    public T create(O object, Inventory inventory, N quantity) {
        validateParams(object, inventory, quantity);
        T stock = newStock(object, inventory, quantity);
        return save(stock);
    }

    @Override
    public T findOne(O object, Inventory inventory) {
        return getStockOrThrow(object, inventory);
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
