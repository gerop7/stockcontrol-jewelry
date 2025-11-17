package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Stone;

@Repository
public interface StoneRepository extends MaterialBaseRepository<Stone> {

    @Query("""
        SELECT DISTINCT s FROM Stone s
        LEFT JOIN FETCH s.stockByInventory st
        LEFT JOIN FETCH st.inventory
        WHERE s.id = :id
    """)
    Optional<Stone> findByIdWithStockByInventory(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT s
        FROM Stone s
        LEFT JOIN FETCH s.user
        LEFT JOIN FETCH s.stockByInventory st
        LEFT JOIN FETCH st.inventory
        LEFT JOIN FETCH s.pendingStoneRestock p
        LEFT JOIN FETCH p.inventory
        WHERE s.id = :id
    """)
    Optional<Stone> findByIdFullData(@Param("id") Long id);

    @Query("""
        SELECT s.id
        FROM Stone s
        JOIN s.stockByInventory st
        WHERE st.inventory.id = :inventoryId
    """)
    Page<Long> findAllIdsByInventoryId(@Param("inventoryId") Long inventoryId, Pageable pageable);

    @Query("""
        SELECT DISTINCT s
        FROM Stone s
        LEFT JOIN FETCH s.user
        LEFT JOIN FETCH s.stockByInventory st
        LEFT JOIN FETCH st.inventory
        LEFT JOIN FETCH s.pendingStoneRestock p
        LEFT JOIN FETCH p.inventory
        WHERE s.id IN :ids
    """)
    List<Stone> findAllByIdsFullData(@Param("ids") List<Long> ids);

    @Query("""
        SELECT s.id
        FROM Stone s
        WHERE s.user.id = :userId
    """)
    Page<Long> findAllIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT DISTINCT s
        FROM Stone s
        LEFT JOIN FETCH s.user
        LEFT JOIN FETCH s.stockByInventory st
        LEFT JOIN FETCH st.inventory
        LEFT JOIN FETCH s.pendingStoneRestock p
        LEFT JOIN FETCH p.inventory
        WHERE s.id IN :ids AND s.user.id = :userId
    """)
    List<Stone> findAllByIdsAndUserFullData(@Param("ids") List<Long> ids, @Param("userId") Long userId);
}
