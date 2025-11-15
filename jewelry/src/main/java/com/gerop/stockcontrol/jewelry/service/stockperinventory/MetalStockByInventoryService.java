package com.gerop.stockcontrol.jewelry.service.stockperinventory;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.MetalStockByInventory;
import com.gerop.stockcontrol.jewelry.repository.MetalStockByInventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetalStockByInventoryService extends AbstractStockByInventoryService<MetalStockByInventory, Metal, Float>{
    private final MetalStockByInventoryRepository repository;

    @Override
    public void remove(MetalStockByInventory stock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public MetalStockByInventory findOne(Metal object, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected MetalStockByInventory getStockOrThrow(Metal object, Inventory inventory) {
        return object.getStockByInventory().stream()
            .filter(s -> s.getInventory().getId().equals(inventory.getId()))
            .findFirst()
            .orElseThrow(() -> new StockNotFoundException(
                "No existe el metal "+object.getName()+" en el inventario "+inventory.getName()+"."));
    }

    @Override
    protected MetalStockByInventory getStockOrCreate(Metal object, Inventory inventory, Float quantity) {
        try {
            return getStockOrThrow(object, inventory);
        }catch (StockNotFoundException e){
            return newStock(object,inventory,quantity);
        }
    }

    @Override
    protected MetalStockByInventory save(MetalStockByInventory stock) {
        return repository.save(stock);
    }

    @Override
    protected MetalStockByInventory newStock(Metal object, Inventory inventory, Float quantity) {
        return new MetalStockByInventory(inventory, object, quantity);
    }

    @Override
    protected String getObjectName(Metal object) {
        return "Metal "+object.getName()+".";
    }

    @Override
    protected void applyAddition(MetalStockByInventory stock, Float quantity) {
        stock.setStock(stock.getStock()+quantity);
    }

    @Override
    protected void applySubtraction(MetalStockByInventory stock, Float quantity) {
        stock.setStock((stock.getStock()-quantity<0)?0f:stock.getStock()-quantity);
    }

}
