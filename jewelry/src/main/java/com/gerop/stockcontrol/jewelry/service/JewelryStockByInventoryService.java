package com.gerop.stockcontrol.jewelry.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;
import com.gerop.stockcontrol.jewelry.repository.JewelryStockByInventoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IStockByInventoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JewelryStockByInventoryService implements IStockByInventoryService<JewelryStockByInventory, Jewel, Long>{
    private final JewelryStockByInventoryRepository repository;

    @Override
    @Transactional
    public JewelryStockByInventory addStock(Jewel jewel, Inventory inventory, Long quantity) {
        if(jewel!=null && inventory!=null){
            if(quantity==null || quantity<0)
                throw new InvalidQuantityException("No se puede agregar esa cantidad de unidades de "+jewel.getSku()+".");
                
            JewelryStockByInventory stock = getStockOrThrow(jewel, inventory);

            stock.setStock(stock.getStock()+quantity);

            return repository.save(stock);
        }else
            throw new RequiredFieldException("Debes completar todos los campos!");
    }

    @Override
    @Transactional
    public JewelryStockByInventory removeStock(Jewel jewel, Inventory inventory, Long quantity) {
        if(jewel!=null && inventory!=null){
            if(quantity==null || quantity<0)
                throw new InvalidQuantityException("No se puede agregar esa cantidad de unidades de "+jewel.getSku()+".");

            JewelryStockByInventory stock = getStockOrThrow(jewel, inventory);
            
            stock.setStock((stock.getStock()-quantity<0)?0L:stock.getStock()-quantity);

            return repository.save(stock);
        }else
            throw new RequiredFieldException("Debes completar todos los campos!");
    }

    private JewelryStockByInventory getStockOrThrow(Jewel jewel, Inventory inventory) {
        return jewel.getStockByInventory().stream()
            .filter(s -> s.getInventory().getId().equals(inventory.getId()))
            .findFirst()
            .orElseThrow(() -> new StockNotFoundException(
                "No existe la joya " + jewel.getSku() + " en el inventario " + inventory.getName()));
    }


    @Override
    @Transactional
    public JewelryStockByInventory create(Jewel jewel, Inventory inventory, Long quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public JewelryStockByInventory findOne(Jewel jewel, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
