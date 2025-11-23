package com.gerop.stockcontrol.jewelry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingStoneRestock;

@Repository
public interface PendingStoneRestockRepository extends JpaRepository<PendingStoneRestock, Long> {

    Optional<PendingStoneRestock> findByStoneIdAndInventoryId(Long entityId, Long inventoryId);

    boolean existsByStoneIdAndInventoryId(Long stoneId, Long inventoryId);


    @Query("""
        SELECT s
        FROM PendingStoneRestock s
        JOIN FETCH s.stone st
        JOIN FETCH s.inventory i
        WHERE st.id = :objId AND i.id = :invId
    """)
    Optional<PendingStoneRestock> findByStoneIdAndInventoryIdFullData(Long objId, Long invId);
}
