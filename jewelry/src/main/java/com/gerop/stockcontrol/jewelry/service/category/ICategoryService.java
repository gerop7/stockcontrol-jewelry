package com.gerop.stockcontrol.jewelry.service.category;

import java.util.List;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.dto.category.ICategoryDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.AbstractCategory;

public interface ICategoryService<C extends AbstractCategory, CDto extends ICategoryDto> {
    CDto create(CDto dto);
    C save(C cat);
    void addToInventories(C cat, List<Inventory> inventories);
    void addToInventories(Long catId, List<Long> inventoriesIds);
    void addToInventory(Long catId, Inventory inventory);
    void removeFromInventories(C cat, List<Inventory> inventories);
    void removeFromInventories(Long catId, List<Long> inventoriesIds);
    void removeFromInventory(Long catId, Inventory inventory);

    List<CDto> findAllByUser(Long userId);
    List<CDto> findAllByInventory(Long inventoryId);
    List<CDto> findAllByUserNotInInventory(Long inventoryId);
    List<CDto> findAllToCreateInInventory(Long inventoryId);
    List<CDto> findAllContainsName(List<CDto> categories, String string);
    Optional<C> findOneWithOwner(Long catId);
    Optional<C> findOne(Long catId);
}
