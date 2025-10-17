package com.gerop.stockcontrol.jewelry.service.permissions;

import com.gerop.stockcontrol.jewelry.model.entity.enums.UserInventoryPermissionType;
import com.gerop.stockcontrol.jewelry.repository.InventoryRepository;
import com.gerop.stockcontrol.jewelry.repository.InventoryUserPermissionsRepository;

public class InventoryPermissionsService implements IInventoryPermissionsService{
    private final InventoryRepository inventoryRepository;
    private final InventoryUserPermissionsRepository permissionsRepository;

    public InventoryPermissionsService(InventoryRepository inventoryRepository,
            InventoryUserPermissionsRepository permissionsRepository) {
        this.inventoryRepository = inventoryRepository;
        this.permissionsRepository = permissionsRepository;
    }

    @Override
    public boolean isOwner(Long inventoryId, Long userId) {
        return inventoryRepository.existsByIdAndOwnerId(inventoryId, userId);
    }

    @Override
    public boolean canRead(Long inventoryId, Long userId) {
        return isOwner(inventoryId, userId) || permissionsRepository.existsByInventoryIdAndUserIdAndType(inventoryId, userId, UserInventoryPermissionType.READ);
    }

    @Override
    public boolean canWrite(Long inventoryId, Long userId) {
        return isOwner(inventoryId, userId) || permissionsRepository.existsByInventoryIdAndUserIdAndType(inventoryId, userId, UserInventoryPermissionType.WRITE);
    }
}
