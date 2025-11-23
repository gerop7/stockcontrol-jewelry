package com.gerop.stockcontrol.jewelry.service.stockperinventory;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;
import com.gerop.stockcontrol.jewelry.repository.JewelryStockByInventoryRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JewelryStockByInventoryService extends AbstractStockByInventoryService<JewelryStockByInventory, Jewel, Long>{
    private final JewelryStockByInventoryRepository repository;
    
    @Override
    public void remove(JewelryStockByInventory stock) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public boolean existByIdAndInventoryId(Long jewelId, Long inventoryId) {
        return repository.existsByJewelIdAndInventoryId(jewelId,inventoryId);
    }

    @Override
    protected JewelryStockByInventory getStockOrThrow(Jewel jewel, Inventory inventory) {
        return repository.findByJewelIdAndInventoryId(jewel.getId(), inventory.getId())
                .orElseThrow(()-> new StockNotFoundException("No existe stock de "+getObjectName(jewel)+" En el inventario "+inventory.getName()+"."));
    }

    @Override
    protected JewelryStockByInventory getStockOrCreate(Jewel jewel, Inventory inventory) {
        try{
            return getStockOrThrow(jewel, inventory);
        } catch (StockNotFoundException e) {
            return newStock(jewel, inventory, 0L);
        }
    }

    @Override
    protected JewelryStockByInventory save(JewelryStockByInventory stock) {
        return repository.save(stock);
    }

    @Override
    protected JewelryStockByInventory newStock(Jewel jewel, Inventory inventory, Long quantity) {
        return new JewelryStockByInventory(inventory, jewel, quantity);
    }

    @Override
    protected String getObjectName(Jewel object) {
        return "Joya "+object.getSku()+".";
    }

    @Override
    protected void applyAddition(JewelryStockByInventory stock, Long quantity) {
        stock.setStock(stock.getStock()+quantity);
    }

    @Override
    protected void applySubtraction(JewelryStockByInventory stock, Long quantity) {
        stock.setStock((stock.getStock()-quantity<0)?0L:stock.getStock()-quantity);
    }

    @Override
    public Optional<JewelryStockByInventory> findOne(Long objId, Long inventoryId) {
        return repository.findByJewelAndInventoryIdFullData(objId,inventoryId);
    }
}
