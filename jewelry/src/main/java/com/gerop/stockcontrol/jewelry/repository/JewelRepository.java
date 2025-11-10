package com.gerop.stockcontrol.jewelry.repository;

import java.util.Optional;

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
}
