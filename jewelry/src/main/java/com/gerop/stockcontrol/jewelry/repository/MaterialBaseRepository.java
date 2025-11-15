package com.gerop.stockcontrol.jewelry.repository;

import com.gerop.stockcontrol.jewelry.model.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaterialBaseRepository<M extends Material> extends CrudRepository<M, Long> {
    Page<Long> findAllIdsByInventoryId(@Param("inventoryId") Long inventoryId, Pageable pageable);
    List<M> findAllByIdsAndInventoryFullData(@Param("ids") List<Long> ids, @Param("inventoryId") Long inventoryId);
    Page<Long> findAllIdsByUserId(@Param("userId") Long userId, Pageable pageable);
    List<M> findAllByIdsAndUserFullData(@Param("ids") List<Long> ids, @Param("userId") Long userId);
    Optional<M> findByIdFullData(@Param("id") Long id);
    Optional<M> findByIdWithStockByInventory(@Param("id") Long materialId);
    boolean existsByIdAndUserId(Long materialId, Long userId);
    boolean existsByIdAndInventoryId(Long materialId, Long inventoryId);
}
