package com.gerop.stockcontrol.jewelry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingMetalRestock;

@Repository
public interface PendingMetalRestockRepository extends JpaRepository<PendingMetalRestock, Long>{

    Optional<PendingMetalRestock> findByMetalIdAndInventoryId(Long entityId, Long inventoryId);

    boolean existsByMetalIdAndInventoryId(Long metalId, Long inventoryId);

    @Query("""
        SELECT s
        FROM PendingMetalRestock s
        JOIN FETCH s.metal m
        JOIN FETCH s.inventory i
        WHERE m.id = :objId AND i.id = :invId
    """)
    Optional<PendingMetalRestock> findByMetalIdAndInventoryIdFullData(Long objId, Long invId);
}
