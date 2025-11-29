package com.gerop.stockcontrol.jewelry.service.permissions;

import java.util.Set;

public interface IJewelPermissionsService {
    boolean canView(Long jewelId, Long inventoryId, Long userId);
    
    void canCreate(Long inventoryId, Long userId, Set<Long> metalIds, Set<Long> stoneIds);
    boolean canModifyStock(Long jewelId, Long inventoryId, Long userId);
    boolean canEditInfo(Long jewelId, Long userId);
    boolean canRemoveFromInventory(Long jewelId, Long ownerId, Long inventoryId);
    boolean canAddToInventory(Long jewelId, Long ownerId, Long inventoryId);

    boolean canDelete(Long jewelId);
    boolean isOwner(Long jewelId, Long userId);

}
