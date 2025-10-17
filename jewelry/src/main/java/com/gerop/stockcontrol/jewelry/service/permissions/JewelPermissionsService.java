package com.gerop.stockcontrol.jewelry.service.permissions;

import com.gerop.stockcontrol.jewelry.repository.JewelRepository;

public class JewelPermissionsService implements IJewelPermissionsService {
    private final JewelRepository jewelRepository;
    private final InventoryPermissionsService invPermissions;

    public JewelPermissionsService(JewelRepository jewelRepository, InventoryPermissionsService invPermissions) {
        this.jewelRepository = jewelRepository;
        this.invPermissions = invPermissions;
    }

    @Override
    public boolean canView(Long jewelId, Long inventoryId, Long userId) {
        
    }

    @Override
    public boolean canModifyStock(Long jewelId, Long inventoryId, Long userId) {
        
    }

    @Override
    public boolean canEditInfo(Long jewelId, Long userId) {
        
    }

    @Override
    public boolean isOwner(Long jewelId, Long userId) {
        
    }

}
