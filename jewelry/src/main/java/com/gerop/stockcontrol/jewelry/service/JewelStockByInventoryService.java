package com.gerop.stockcontrol.jewelry.service;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;
import com.gerop.stockcontrol.jewelry.service.interfaces.IJewelStockByInventoryService;

@Service
public class JewelStockByInventoryService implements IJewelStockByInventoryService{

    @Override
    public JewelryStockByInventory create(Long inventoryId, Long id, Long stock) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

}
