package com.gerop.stockcontrol.jewelry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingJewelRestock;

@Repository
public interface PendingJewelRestockRepository extends JpaRepository<PendingJewelRestock, Long> {

    Optional<PendingJewelRestock> findByJewelIdAndInventoryId(Long jewelId, Long inventoryId);

    boolean existsByJewelIdAndInventoryId(Long jewelId, Long inventoryId);

    @Query("""
        SELECT s
        FROM PendingJewelRestock s
        JOIN FETCH s.jewel j
        JOIN FETCH s.inventory i
        WHERE j.id = :objId AND i.id = :invId
    """)
    Optional<PendingJewelRestock> findByJewelIdAndInventoryIdFullData(Long objId, Long invId);
}
