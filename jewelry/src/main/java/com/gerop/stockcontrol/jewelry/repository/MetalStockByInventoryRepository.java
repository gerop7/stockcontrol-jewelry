package com.gerop.stockcontrol.jewelry.repository;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.MetalStockByInventory;

import java.util.Optional;

@Repository
public interface MetalStockByInventoryRepository extends JpaRepository<MetalStockByInventory, Long> {

    boolean existsByInventoryIdAndMetalId(Long materialId, Long inventoryId);

    Optional<MetalStockByInventory> findByMetalAndInventory(Metal object, Inventory inventory);
}
