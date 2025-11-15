package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Metal;

@Repository
public interface MetalRepository extends MaterialBaseRepository<Metal>{
    @Query("""
        SELECT DISTINCT m FROM Metal m
        LEFT JOIN FETCH m.stockByInventory s
        LEFT JOIN FETCH s.inventory
        WHERE m.id = :id
    """)
    Optional<Metal> findByIdWithStockByInventory(@Param("id") Long metalId);

    @Query("""
        SELECT DISTINCT m FROM Metal m
        LEFT JOIN FETCH m.user
        LEFT JOIN FETCH m.stockByInventory s
        LEFT JOIN FETCH s.inventory
        LEFT JOIN FETCH m.pendingMetalRestock p
        LEFT JOIN FETCH p.inventory
        WHERE m.id = :id
    """)
    Optional<Metal> findByIdFullData(@Param("id") Long id);

    @Query("""
        SELECT m.id FROM Metal m
        JOIN m.stockByInventory s
        WHERE s.inventory.id = :inventoryId
    """)
    Page<Long> findAllIdsByInventoryId(@Param("inventoryId") Long inventoryId, Pageable pageable);

    @Query("""
        SELECT DISTINCT m
        FROM Metal m
        LEFT JOIN FETCH m.user
        LEFT JOIN FETCH m.stockByInventory s
        LEFT JOIN FETCH s.inventory
        LEFT JOIN FETCH m.pendingMetalRestock p
        LEFT JOIN FETCH p.inventory
        WHERE m.id IN :ids AND s.inventory.id = :inventoryId
    """)
    List<Metal> findAllByIdsAndInventoryFullData(@Param("ids") List<Long> ids, @Param("inventoryId") Long inventoryId);

    @Query("""
        SELECT m.id
        FROM Metal m
        WHERE m.user.id = :userId
    """)
    Page<Long> findAllIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT DISTINCT m
        FROM Metal m
        LEFT JOIN FETCH m.user
        LEFT JOIN FETCH m.stockByInventory s
        LEFT JOIN FETCH s.inventory
        LEFT JOIN FETCH m.pendingMetalRestock p
        LEFT JOIN FETCH p.inventory
        WHERE m.id IN :ids AND m.user.id = :userId
    """)
    List<Metal> findAllByIdsAndUserFullData(@Param("ids") List<Long> ids, @Param("userId") Long userId);
}
