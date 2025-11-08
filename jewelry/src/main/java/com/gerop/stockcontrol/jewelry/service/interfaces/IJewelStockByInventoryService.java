package com.gerop.stockcontrol.jewelry.service.interfaces;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;

public interface IJewelStockByInventoryService {

    JewelryStockByInventory create(Inventory inventory, Jewel jewel, Long stock);

}
