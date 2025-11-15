package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;


@Repository
public interface JewelRepository extends JpaRepository<Jewel, Long> {

    boolean existsBySkuAndUserId(String sku,Long id);
    Optional<Jewel> findBySkuAndUserId(String sku, Long id);
    Optional<Jewel> findByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long jewelId, Long userId);

    @Query("""
        SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END
        FROM Jewel j
        JOIN j.stone s
        WHERE j.id = :jewelId
    """)
    boolean existsByIdAndHasStones(@Param("jewelId") Long jewelId);
    boolean existsByIdAndMetal_Id(Long jewelId, Long metalId);
    boolean existsByIdAndStone_Id(Long jewelId, Long stoneId);

    @Query("""
        SELECT j FROM Jewel j
        LEFT JOIN FETCH j.metal
        LEFT JOIN FETCH j.stone
        WHERE j.id = :id
    """)
    Optional<Jewel> findByIdWithMaterials(Long id);

    @Query("""
        SELECT j FROM Jewel j
        LEFT JOIN FETCH j.inventories i
        WHERE j.id = :id
    """)
    Optional<Jewel> findByIdWithInventories(Long id);

    @Query("""
        SELECT j FROM Jewel j
        LEFT JOIN FETCH j.stockByInventory s
        LEFT JOIN FETCH s.inventory i
        WHERE j.id = :id
    """)
    public Optional<Jewel> findByIdWithStockByInventory(Long id);

    @Query("""
    SELECT DISTINCT j FROM Jewel j
    LEFT JOIN FETCH j.category
    LEFT JOIN FETCH j.subcategory
    LEFT JOIN FETCH j.metal
    LEFT JOIN FETCH j.stone
    LEFT JOIN FETCH j.stockByInventory s
    LEFT JOIN FETCH s.inventory
    LEFT JOIN FETCH j.pendingRestock p
    LEFT JOIN FETCH p.inventory
    LEFT JOIN FETCH j.inventories
    LEFT JOIN FETCH j.user
    WHERE j.id = :id
""")
    Optional<Jewel> findByIdFullData(@Param("id") Long id);

    @Query("""
        SELECT j.id
        FROM Jewel j
        JOIN j.inventories i
        WHERE j.active = true AND i.id = :inventoryId
    """)
    Page<Long> findAllIdsByInventoryId(@Param("inventoryId") Long inventoryId, Pageable pageable);

    @Query("""
        SELECT DISTINCT j FROM Jewel j
        LEFT JOIN FETCH j.category
        LEFT JOIN FETCH j.subcategory
        LEFT JOIN FETCH j.metal
        LEFT JOIN FETCH j.stone
        LEFT JOIN FETCH j.inventories inv
        LEFT JOIN FETCH j.stockByInventory s ON s.inventory.id = :inventoryId
        LEFT JOIN FETCH j.pendingRestock p ON p.inventory.id = :inventoryId
        WHERE j.id IN :ids
    """)
    List<Jewel> findAllByIdsWithFullData(@Param("ids") List<Long> ids, @Param("inventoryId") Long inventoryId);
    
    @Query("""
        SELECT j.id
        FROM Jewel j
        WHERE j.active = true AND j.user.id = :userId
    """)
    Page<Long> findAllIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT DISTINCT j FROM Jewel j
        LEFT JOIN FETCH j.category
        LEFT JOIN FETCH j.subcategory
        LEFT JOIN FETCH j.metal
        LEFT JOIN FETCH j.stone
        LEFT JOIN FETCH j.stockByInventory s
        LEFT JOIN FETCH s.inventory
        LEFT JOIN FETCH j.pendingRestock p
        LEFT JOIN FETCH p.inventory
        LEFT JOIN FETCH j.inventories
        WHERE j.user.id = :userId AND j.active = true AND j.id IN :ids
    """)
    List<Jewel> findAllByIdsWithFullDataByUser(@Param("ids") List<Long> ids, @Param("userId") Long userId);
}
