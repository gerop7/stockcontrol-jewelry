package com.gerop.stockcontrol.jewelry.service.stockperinventory;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.StoneStockByInventory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoneStockByInventoryService implements IStockByInventoryService<StoneStockByInventory, Stone, Long>{

    @Override
    public StoneStockByInventory create(Stone object, Inventory inventory, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(StoneStockByInventory stock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addStock(Stone object, Inventory inventory, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeStock(Stone object, Inventory inventory, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StoneStockByInventory findOne(Stone object, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
