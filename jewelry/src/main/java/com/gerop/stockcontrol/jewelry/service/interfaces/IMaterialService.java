package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.gerop.stockcontrol.jewelry.model.dto.materials.MaterialDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Material;
import org.springframework.data.domain.Page;

public interface IMaterialService<M extends Material, Q extends Number, MDto extends MaterialDto> {
    MDto create(MDto material);
    boolean delete(Long materialId);
    boolean delete(M material);
    M save(M material);
    void update(MDto updateData);

    void addStock(Long materialId, Long inventoryId, Q quantity, String description);
    void addStock(M material, Inventory inventory, Q quantity, String description);
    void outflowByWork(Long materialId, Long inventoryId, Q quantity, Q quantityToRestock);
    void outflowByWork(M material, Inventory inventory, Q quantity, Q quantityToRestock);
    MDto sale(Long materialId, Long inventoryId, Q quantity, Float total, Q quantityToRestock);


    void addToInventory(Long materialId, Inventory inventory, Q quantity);
    void removeFromInventory(Long materialId, Inventory inventory);

    void addPendingToRestock(Long materialId, Q quantity, Long inventoryId);
    void addPendingToRestock(M material, Q quantity, Inventory inventory);
    void removePendingToRestock(Long materialId, Q quantity, Long inventoryId);
    void removePendingToRestock(M material, Q quantity, Inventory inventory);

    Optional<M> findOne(Long materialId);
    List<M> findAllByIds(Set<Long> materialIds);
    Page<M> findAllByInventory(Long inventoryId, int page, int size);
    Page<M> findAllByCurrentUser(int page, int size);

    Optional<MDto> findOneDto(Long materialId);
    Page<MDto> findAllByInventoryDto(Long inventoryId, int page, int size);
    Page<MDto> findAllByCurrentUserDto(int page, int size);

}
