package com.gerop.stockcontrol.jewelry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gerop.stockcontrol.jewelry.model.entity.Stone;

public interface StoneStockByInventoryRepository extends JpaRepository<Stone, Long>{

    boolean existsByStoneIdAndInventoryId(Long materialId, Long inventoryId);

}
