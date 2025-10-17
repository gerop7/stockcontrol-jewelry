package com.gerop.stockcontrol.jewelry.service.permissions;

public interface IMaterialPermissionsService<Material> {
    boolean isOwner(Long materialId, Long userId);
    boolean canUseToCreate(Long materialId, Long userId, Long inventoryId);
    boolean canUpdateStockByInventory(Long materialId, Long userId, Long inventoryId);
}
