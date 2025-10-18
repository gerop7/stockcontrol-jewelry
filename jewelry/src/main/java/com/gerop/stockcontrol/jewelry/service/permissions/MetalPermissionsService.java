package com.gerop.stockcontrol.jewelry.service.permissions;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.repository.MetalRepository;
import com.gerop.stockcontrol.jewelry.repository.MetalStockByInventoryRepository;

@Service
public class MetalPermissionsService implements IMaterialPermissionsService<Metal> {
    private final MetalRepository metalRepository;
    private final IInventoryPermissionsService invPermissions;
    private final MetalStockByInventoryRepository metalStockRepository;

    public MetalPermissionsService(InventoryPermissionsService invPermissions, MetalRepository metalRepository, MetalStockByInventoryRepository metalStockRepository) {
        this.invPermissions = invPermissions;
        this.metalRepository = metalRepository;
        this.metalStockRepository = metalStockRepository;
    }

    @Override
    public boolean isOwner(Long materialId, Long userId) {
        return metalRepository.existsByIdAndUserId(materialId,userId);
    }

    @Override
    public boolean canUseToCreate(Long materialId, Long userId, Long inventoryId) {
        if(!metalStockRepository.existsByInventoryIdAndMetalId(inventoryId, materialId)) return false;

        return isOwner(materialId, userId) || invPermissions.canWrite(inventoryId, userId);
    }

    @Override
    public boolean canUpdateStockByInventory(Long materialId, Long userId, Long inventoryId) {
        return canUseToCreate(materialId, userId, inventoryId);
    }
}
