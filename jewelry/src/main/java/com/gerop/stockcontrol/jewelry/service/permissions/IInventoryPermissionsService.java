package com.gerop.stockcontrol.jewelry.service.permissions;

public interface IInventoryPermissionsService {
    boolean isOwner(Long inventoryId, Long userId);
    boolean canRead(Long inventoryId, Long userId);
    boolean canWrite(Long inventoryId, Long userId);
}
