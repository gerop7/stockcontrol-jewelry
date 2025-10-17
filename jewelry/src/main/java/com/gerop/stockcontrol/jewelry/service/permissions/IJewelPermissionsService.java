package com.gerop.stockcontrol.jewelry.service.permissions;

public interface IJewelPermissionsService {
    boolean canView(Long jewelId, Long inventoryId, Long userId);
    boolean canModifyStock(Long jewelId, Long inventoryId, Long userId);
    boolean canEditInfo(Long jewelId, Long userId);
    boolean isOwner(Long jewelId, Long userId);
    boolean canAddToInventory(Long jewelId, Long userId, Long inventoryId);
}
