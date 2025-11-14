package com.gerop.stockcontrol.jewelry.service.stockperinventory;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.StoneStockByInventory;
import com.gerop.stockcontrol.jewelry.repository.StoneStockByInventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoneStockByInventoryService extends AbstractStockByInventoryService<StoneStockByInventory, Stone, Long>{

    private final StoneStockByInventoryRepository repository;

    @Override
    public void remove(StoneStockByInventory stock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StoneStockByInventory findOne(Stone object, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected StoneStockByInventory getStockOrThrow(Stone object, Inventory inventory) {
        return object.getStockByInventory().stream()
            .filter(s -> s.getInventory().getId().equals(inventory.getId()))
            .findFirst()
            .orElseThrow(() -> new StockNotFoundException(
                "No existe la priedra "+object.getName()+" en el inventario "+inventory.getName()+"."));
    }

    @Override
    protected StoneStockByInventory save(StoneStockByInventory stock) {
        return repository.save(stock);
    }

    @Override
    protected StoneStockByInventory newStock(Stone object, Inventory inventory, Long quantity) {
        return new StoneStockByInventory(inventory, quantity, object);
    }

    @Override
    protected String getObjectName(Stone object) {
        return "Stone "+object.getName()+".";
    }

    @Override
    protected void applyAddition(StoneStockByInventory stock, Long quantity) {
        stock.setStock(stock.getStock()+quantity);
    }

    @Override
    protected void applySubtraction(StoneStockByInventory stock, Long quantity) {
        stock.setStock((stock.getStock()-quantity<0)?0L:stock.getStock()-quantity);
    }

    
}
