package com.gerop.stockcontrol.jewelry.service.stockperinventory;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.MetalStockByInventory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetalStockByInventoryService implements IStockByInventoryService<MetalStockByInventory, Metal, Long> {

    @Override
    public MetalStockByInventory create(Metal object, Inventory inventory, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(MetalStockByInventory stock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addStock(Metal object, Inventory inventory, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeStock(Metal object, Inventory inventory, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MetalStockByInventory findOne(Metal object, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
