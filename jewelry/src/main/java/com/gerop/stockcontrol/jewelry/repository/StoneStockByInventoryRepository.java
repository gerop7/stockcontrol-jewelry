package com.gerop.stockcontrol.jewelry.repository;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.StoneStockByInventory;

import java.util.Optional;

@Repository
public interface StoneStockByInventoryRepository extends JpaRepository<StoneStockByInventory, Long>{
    boolean existsByStoneIdAndInventoryId(Long materialId, Long inventoryId);

    Optional<StoneStockByInventory> findByStoneAndInventory(Stone object, Inventory inventory);

    @Query("""
        SELECT s
        FROM StoneStockByInventory s
        JOIN FETCH s.stone st
        JOIN FETCH s.inventory i
        WHERE st.id = :objId AND i.id = :inventoryId
        """)
    Optional<StoneStockByInventory> findByStoneAndInventoryIdFullData(Long objId, Long inventoryId);
}
