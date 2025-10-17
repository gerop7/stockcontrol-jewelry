package com.gerop.stockcontrol.jewelry.service.permissions;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.repository.MetalRepository;
import com.gerop.stockcontrol.jewelry.repository.MetalStockByInventoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MetalPermissionsService implements IMaterialPermissionsService<Metal> {
    private final MetalRepository metalRepository;
    private final InventoryPermissionsService invPermissions;
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
        if(isOwner(materialId, userId)) return true;

        Long metalOwnerId = metalRepository.findUserIdByMetalId(materialId).orElseThrow(() -> new EntityNotFoundException("Metal not found"));
        if(metalOwnerId == null) return false;

        return invPermissions.canWrite(inventoryId, userId) && invPermissions.isOwner(inventoryId, metalOwnerId);
    }

    @Override
    public boolean canUpdateStockByInventory(Long materialId, Long userId, Long inventoryId) {
        if(!metalStockRepository.existsByInventoryIdAndMetalId(inventoryId, materialId)) return false;

        return canUseToCreate(materialId, userId, inventoryId);
    }

}
