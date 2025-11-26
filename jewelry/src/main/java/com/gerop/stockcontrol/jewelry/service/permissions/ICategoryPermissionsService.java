package com.gerop.stockcontrol.jewelry.service.permissions;

public interface ICategoryPermissionsService<O extends Object> {
    boolean isOwner(Long ownerId, Long catId);
    boolean isOwner(Long ownerId, O cat);
    boolean canCreate(Long userId, Long inventoryId);
    boolean canDeleteFromInventory(Long categoryId, Long inventoryId, Long userId);
}
