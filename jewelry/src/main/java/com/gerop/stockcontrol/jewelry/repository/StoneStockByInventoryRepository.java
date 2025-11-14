package com.gerop.stockcontrol.jewelry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.StoneStockByInventory;

@Repository
public interface StoneStockByInventoryRepository extends JpaRepository<StoneStockByInventory, Long>{
    boolean existsByStoneIdAndInventoryId(Long materialId, Long inventoryId);

}
