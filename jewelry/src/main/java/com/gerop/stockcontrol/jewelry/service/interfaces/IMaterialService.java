package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.gerop.stockcontrol.jewelry.model.dto.UpdateMaterialDataDto;
import com.gerop.stockcontrol.jewelry.model.dto.materials.MaterialDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Material;

public interface IMaterialService<M extends Material, Q extends Number, MDto extends MaterialDto> {
    MDto create(MDto material);
    boolean delete(Long materialId);
    boolean delete(M material);
    MDto save(M material);
    void update(Long materialId, UpdateMaterialDataDto data);

    void addStock(Long materialid, Long inventoryId, Q quantity, String description);
    void addStock(M material, Inventory inventory, Q quantity, String description);
    void outflowByWork(Long materialid, Long inventoryId, Q quantity);
    void outflowByWork(M material, Inventory inventory, Q quantity);
    MDto sale(Long materialid, Long inventoryId, Q quantity);

    void addToInventory(Long materialid, Inventory inventory);
    void removeFromInventory(Long materialid, Inventory inventory);

    void addPendingToRestock(Long materialId, Q quantity, Inventory inventory);
    void addPendingToRestock(M material, Q quantity, Inventory inventory);
    void removePendingToRestock(Long materialId, Q quantity, Long inventoryId);
    void removePendingToRestock(M material, Q quantity, Long inventoryId);

    Optional<M> findOne(Long materialId);
    List<M> findAllByIds(Set<Long> materialIds);

    boolean canUseToCreate(Long materialId, Long userId, Long inventoryId);
    boolean canAddToInventory(Long materialId, Long userId, Long inventoryId);
}
