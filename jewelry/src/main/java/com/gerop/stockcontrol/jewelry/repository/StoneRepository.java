package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    @Override
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
    @Override
    Optional<Stone> findByIdFullData(@Param("id") Long id);

    @Override
    @Query("""
        SELECT DISTINCT s FROM Stone s
        LEFT JOIN FETCH s.user u
    
        LEFT JOIN FETCH s.stockByInventory st
        LEFT JOIN FETCH st.inventory sti
    
        LEFT JOIN FETCH s.pendingStoneRestock p
        LEFT JOIN FETCH p.inventory pi
    
        WHERE s.id = :materialId
        AND (st.inventory.id IN :inventoriesIds OR st.inventory IS NULL)
        AND (p.inventory.id IN :inventoriesIds OR p.inventory IS NULL)
    """)
    Optional<Stone> findByIdAndInventoriesIdsFullData(Long materialId, Set<Long> inventoriesIds);

    @Override
    @Query("""
        SELECT s FROM Stone s
        JOIN s.stockByInventory st
        WHERE st.inventory.id = :inventoryId
    """)
    List<Stone> findAllByInventoryId(Long inventoryId);

    @Override
    @Query("""
            SELECT DISTINCT s FROM Stone s
            LEFT JOIN FETCH s.user u
            LEFT JOIN FETCH s.stockByInventory st
            LEFT JOIN FETCH st.inventory sti
            LEFT JOIN FETCH s.pendingStoneRestock p
            LEFT JOIN FETCH p.inventory pi
            WHERE (st.inventory.id = :inventoryId OR st.inventory IS NULL)
            AND (p.inventory.id = :inventoryId OR p.inventory IS NULL)
    """)
    List<Stone> findAllByInventoryFullData(Long inventoryId);

    @Override
    @Query("""
        SELECT s FROM Stone s
        JOIN s.user u
        WHERE u.id = :id OR s.global = true
    """)
    List<Stone> findAllByOwnerId(Long id);

    @Override
    @Query("""
        SELECT DISTINCT s FROM Stone s
        JOIN s.user u
    
        LEFT JOIN FETCH s.stockByInventory st
        LEFT JOIN FETCH st.inventory sti
    
        LEFT JOIN FETCH s.pendingStoneRestock p
        LEFT JOIN FETCH p.inventory pi
    
        WHERE (s.user.id = :ownerId OR s.isGlobal = true)
        AND (st.inventory.id IN :inventoriesIds OR st.inventory IS NULL)
        AND (p.inventory.id IN :inventoriesIds OR p.inventory IS NULL)
    """)
    List<Stone> findAllByOwnerIdFullData(Long ownerId, Set<Long> inventoriesIds);

    @Override
    @Query("""
    SELECT s FROM Stone s
    WHERE s.user.id = :ownerId AND s.isGlobal = false
      AND s.id NOT IN (
            SELECT st.stone.id FROM StoneStockByInventory st
            WHERE st.inventory.id = :inventoryId
      )
    """)
    List<Stone> findAllByOwnerIdNotInInventory(Long ownerId, Long inventoryId);
}
