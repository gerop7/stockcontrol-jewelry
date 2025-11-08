package com.gerop.stockcontrol.jewelry.service.interfaces;

import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;

public interface IJewelStockByInventoryService {

    JewelryStockByInventory create(Long inventoryId, Long id, Long stock);

}
