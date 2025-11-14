package com.gerop.stockcontrol.jewelry.service.permissions;


import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.exception.material.MaterialNotFoundException;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryUserPermissionType;
import com.gerop.stockcontrol.jewelry.repository.MetalRepository;
import com.gerop.stockcontrol.jewelry.repository.MetalStockByInventoryRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

@Service
public class MetalPermissionsService implements IMaterialPermissionsService<Metal> {

    private final UserServiceHelper userServiceHelper;
    private final MetalRepository metalRepository;
    private final IInventoryPermissionsService invPermissions;
    private final MetalStockByInventoryRepository metalStockRepository;

    public MetalPermissionsService(InventoryPermissionsService invPermissions, MetalRepository metalRepository, 
        MetalStockByInventoryRepository metalStockRepository, UserServiceHelper userServiceHelper) 
        {
        this.invPermissions = invPermissions;
        this.metalRepository = metalRepository;
        this.metalStockRepository = metalStockRepository;
        this.userServiceHelper = userServiceHelper;
    }

    @Override
    public boolean isOwner(Long materialId, Long userId) {
        return metalRepository.existsByIdAndUserId(materialId,userId);
    }

    @Override
    public boolean canView(Long materialId, Long inventoryId, Long userId) {
        return metalRepository.existsByIdAndInventoryId(materialId, inventoryId) && invPermissions.canRead(inventoryId, userId);
    }

    @Override
    public boolean canAddToInventory(Long materialId, Long userId, Long inventoryId) {
        Metal metal = metalRepository.findById(materialId)
                .orElseThrow(() -> new MaterialNotFoundException("Metal no encontrado"));
        return (metal.isGlobal() || isOwner(materialId, userId)) 
            && invPermissions.canWrite(inventoryId, userId) && !metalStockRepository.existsByInventoryIdAndMetalId(inventoryId, materialId);
    }

    @Override
    public boolean canUseToCreate(Long materialId, Long userId, Long inventoryId) {
        return canUseToCreateWithoutInvPermission(materialId, userId, inventoryId) && invPermissions.canWrite(inventoryId, userId);
    }

    @Override
    public boolean canUseToCreateWithoutInvPermission(Long materialId, Long userId, Long inventoryId) {
        Metal metal = metalRepository.findById(materialId)
                .orElseThrow(() -> new MaterialNotFoundException("Metal no encontrado"));

        return metal.isGlobal() || 
            (isOwner(materialId, userId) || metalStockRepository.existsByInventoryIdAndMetalId(inventoryId, materialId));
    }
    
    @Override
    public boolean canUpdateStock(Long materialId, Long userId, Long inventoryId) {
        return invPermissions.canWrite(inventoryId, userId) && metalStockRepository.existsByInventoryIdAndMetalId(inventoryId, materialId);
    }

    

    @Override
    public boolean canDeleteFromInventory(Long materialId, Long inventoryId) {
        Long currentUserId = userServiceHelper.getCurrentUser().getId();
        Metal metal = metalRepository.findById(materialId)
                .orElseThrow(() -> new MaterialNotFoundException("Metal no encontrado"));

        if(metal.isGlobal()) return false;
        
        return (isOwner(materialId, currentUserId) || invPermissions.isOwner(inventoryId, currentUserId)) && 
            metalStockRepository.existsByInventoryIdAndMetalId(inventoryId, materialId);
    }

    @Override
    public boolean canCreate(Long materialId, Long inventoryId) {
        invPermissions.validatePermission(inventoryId, userServiceHelper.getCurrentUser().getId(), InventoryUserPermissionType.WRITE, "Crear una joya");
        return true;
    }
}
