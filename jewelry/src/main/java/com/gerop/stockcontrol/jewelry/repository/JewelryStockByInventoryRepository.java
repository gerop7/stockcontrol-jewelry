package com.gerop.stockcontrol.jewelry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;

@Repository
public interface JewelryStockByInventoryRepository extends JpaRepository<JewelryStockByInventory, Long>{

    boolean existsByJewelIdAndInventoryId(Long jewelId, Long inventoryId);

}
