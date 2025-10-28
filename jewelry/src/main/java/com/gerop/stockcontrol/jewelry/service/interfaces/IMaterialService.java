package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.gerop.stockcontrol.jewelry.model.dto.MaterialDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateMaterialDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Material;

public interface IMaterialService<M extends Material, Q extends Number, MDto extends MaterialDto> {
    M create(MDto material);
    M save(M material);
    MDto update(Long materialId, UpdateMaterialDataDto data);

    MDto addStock(Long materialid, Long inventoryId, Q quantity, String description);
    MDto sale(Long materialid, Long inventoryId, Q quantity);

    void addPendingToRestock(Long materialId, Q quantity, Long inventoryId);
    void removePendingToRestock(Long materialId, Q quantity, Long inventoryId);

    Optional<M> findOne(Long materialId);
    List<M> findAllByIds(Set<Long> materialIds);

    boolean canUseToCreate(Long materialId, Long userId, Long inventoryId);
    boolean canAddToInventory(Long materialId, Long userId, Long inventoryId);
}
