package com.gerop.stockcontrol.jewelry.service.stockperinventory;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;
import com.gerop.stockcontrol.jewelry.repository.JewelryStockByInventoryRepository;

import lombok.RequiredArgsConstructor;

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
    public JewelryStockByInventory findOne(Jewel jewel, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected JewelryStockByInventory getStockOrThrow(Jewel jewel, Inventory inventory) {
        return jewel.getStockByInventory().stream()
            .filter(s -> s.getInventory().getId().equals(inventory.getId()))
            .findFirst()
            .orElseThrow(() -> new StockNotFoundException(
                "No existe la joya " + jewel.getSku() + " en el inventario " + inventory.getName()));
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
}
