package com.gerop.stockcontrol.jewelry.service.stockperinventory;

import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.StoneStockByInventory;
import com.gerop.stockcontrol.jewelry.repository.StoneStockByInventoryRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoneStockByInventoryService extends AbstractStockByInventoryService<StoneStockByInventory, Stone, Long>{

    private final StoneStockByInventoryRepository repository;

    @Override
    public void remove(StoneStockByInventory stock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<StoneStockByInventory> findOne(Stone object, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean existByIdAndInventoryId(Long materialId, Long id) {
        return repository.existsByStoneIdAndInventoryId(materialId,id);
    }

    @Override
    protected StoneStockByInventory getStockOrThrow(Stone object, Inventory inventory) {
        return repository.findByStoneAndInventory(object,inventory)
            .orElseThrow(() -> new StockNotFoundException(
                "No existe la priedra "+object.getName()+" En el inventario "+inventory.getName()+"."));
    }

    @Override
    protected StoneStockByInventory getStockOrCreate(Stone object, Inventory inventory) {
        try {
            return getStockOrThrow(object, inventory);
        }catch (StockNotFoundException e){
            return newStock(object,inventory,0L);
        }
    }

    @Override
    protected StoneStockByInventory save(StoneStockByInventory stock) {
        return repository.save(stock);
    }

    @Override
    protected StoneStockByInventory newStock(Stone object, Inventory inventory, Long quantity) {
        return new StoneStockByInventory(inventory, object, quantity);
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

    @Override
    public Optional<StoneStockByInventory> findOne(Long objId, Long inventoryId) {
        return repository.findByStoneAndInventoryIdFullData(objId,inventoryId);
    }
}
