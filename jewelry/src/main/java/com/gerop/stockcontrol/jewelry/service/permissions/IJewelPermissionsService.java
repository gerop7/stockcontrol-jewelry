package com.gerop.stockcontrol.jewelry.service.permissions;

public interface IJewelPermissionsService {
    boolean canView(Long jewelId, Long inventoryId, Long userId);
    
    boolean canModifyStock(Long jewelId, Long inventoryId, Long userId);
    boolean canEditInfo(Long jewelId, Long userId);
    boolean canAddToInventory(Long jewelId, Long ownerId, Long inventoryId);

    boolean canDelete(Long jewelId);
    boolean isOwner(Long jewelId, Long userId);
}
