package com.gerop.stockcontrol.jewelry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>{

    boolean existsByIdAndOwnerId(Long inventoryId, Long userId);

}
