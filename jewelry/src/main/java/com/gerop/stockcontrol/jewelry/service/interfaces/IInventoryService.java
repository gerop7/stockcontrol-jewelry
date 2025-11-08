package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;

public interface IInventoryService {

    public Optional<Inventory> findOne(Long inventoryId);

}
