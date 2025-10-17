package com.gerop.stockcontrol.jewelry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.MetalStockByInventory;

@Repository
public interface MetalStockByInventoryRepository extends JpaRepository<MetalStockByInventory, Long> {

    public boolean existsByInventoryIdAndMetalId(Long materialId, Long inventoryId);

}
