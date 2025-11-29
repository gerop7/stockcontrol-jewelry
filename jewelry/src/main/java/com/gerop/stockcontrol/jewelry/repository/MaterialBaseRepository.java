package com.gerop.stockcontrol.jewelry.repository;

import com.gerop.stockcontrol.jewelry.model.entity.Material;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MaterialBaseRepository<M extends Material> extends CrudRepository<M, Long> {
    Optional<M> findByIdFullData(@Param("id") Long id);
    Optional<M> findByIdWithStockByInventory(@Param("id") Long materialId);
    boolean existsByIdAndUserId(Long materialId, Long userId);
    boolean existsByIdAndInventoryId(Long materialId, Long inventoryId);

    Optional<M> findByIdAndInventoriesIdsFullData(Long materialId, Set<Long> inventoriesIds);
    List<M> findAllByInventoryId(Long inventoryId);
    List<M> findAllByInventoryFullData(Long inventoryId);
    List<M> findAllByOwnerId(Long id);
    List<M> findAllByOwnerIdFullData(Long id, Set<Long> inventoriesIds);

    List<M> findAllByOwnerIdNotInInventory(Long ownerId, Long inventoryId);
}
