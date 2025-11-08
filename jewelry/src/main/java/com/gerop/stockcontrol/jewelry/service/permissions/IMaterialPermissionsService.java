package com.gerop.stockcontrol.jewelry.service.permissions;

public interface IMaterialPermissionsService<Material> {
    boolean isOwner(Long materialId, Long userId);
    boolean canView(Long materialId, Long inventoryId, Long userId);
    boolean canAddToInventory(Long materialId, Long userId, Long inventoryId);
    boolean canUseToCreate(Long materialId, Long userId, Long inventoryId);
    boolean canUseToCreateWithoutInvPermission(Long materialId, Long userId, Long inventoryId);
    boolean canUpdateStock(Long materialId, Long userId, Long inventoryId);
    boolean canDeleteFromInventory(Long materialId, Long inventoryId);
}
