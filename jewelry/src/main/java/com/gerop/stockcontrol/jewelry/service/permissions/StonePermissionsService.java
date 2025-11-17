package com.gerop.stockcontrol.jewelry.service.permissions;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryUserPermissionType;
import com.gerop.stockcontrol.jewelry.repository.StoneRepository;
import com.gerop.stockcontrol.jewelry.repository.StoneStockByInventoryRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StonePermissionsService implements IMaterialPermissionsService<Stone> {

    private final StoneRepository stoneRepository;
    private final IInventoryPermissionsService invPermissions;
    private final StoneStockByInventoryRepository stoneStockRepository;
    private final UserServiceHelper userServiceHelper;

    public StonePermissionsService(StoneRepository stoneRepository, IInventoryPermissionsService invPermissions,
            StoneStockByInventoryRepository stoneStockRepository, UserServiceHelper userServiceHelper) {
        this.stoneRepository = stoneRepository;
        this.invPermissions = invPermissions;
        this.stoneStockRepository = stoneStockRepository;
        this.userServiceHelper = userServiceHelper;
    }

    @Override
    public boolean isOwner(Long materialId, Long userId) {
        return stoneRepository.existsByIdAndUserId(materialId, userId);
    }

    @Override
    public boolean canView(Long materialId, Long inventoryId, Long userId) {
        return stoneStockRepository.existsByStoneIdAndInventoryId(materialId, inventoryId) && invPermissions.canRead(inventoryId, userId);
    }

    @Override
    public boolean canAddToInventory(Long materialId, Long userId, Long inventoryId) {
        Stone stone = stoneRepository.findById(materialId)
            .orElseThrow(() -> new EntityNotFoundException("Piedra no encontrada"));
        
        return (stone.isGlobal() || isOwner(materialId, userId)) && invPermissions.canWrite(inventoryId, userId) && stoneStockRepository.existsByStoneIdAndInventoryId(materialId,inventoryId);
    }

    @Override
    public boolean canUseToCreate(Long materialId, Long userId, Long inventoryId) {
         return canUseToCreateWithoutInvPermission(materialId,userId,inventoryId) && invPermissions.canWrite(inventoryId, userId);
    }

    public boolean canUseToCreateWithoutInvPermission(Long materialId, Long userId, Long inventoryId){
        Stone stone = stoneRepository.findById(materialId)
            .orElseThrow(() -> new EntityNotFoundException("Piedra no encontrada"));

        return stone.isGlobal() || 
                (isOwner(materialId, userId) || stoneStockRepository.existsByStoneIdAndInventoryId(materialId, inventoryId));
    }

    @Override
    public boolean canUpdateStock(Long materialId, Long userId, Long inventoryId) {
        return invPermissions.canWrite(inventoryId, userId);
    }

    @Override
    public boolean canDeleteFromInventory(Long materialId, Long inventoryId) {
        Long currentUserId = userServiceHelper.getCurrentUser().getId();
        Stone stone = stoneRepository.findById(materialId)
            .orElseThrow(() -> new EntityNotFoundException("Piedra no encontrada"));

        if(stone.isGlobal()) return false;

        return (isOwner(materialId, currentUserId) || invPermissions.isOwner(inventoryId, currentUserId));
    }

    @Override
    public boolean canCreate(Long inventoryId) {
        invPermissions.validatePermission(inventoryId, userServiceHelper.getCurrentUser().getId(), InventoryUserPermissionType.WRITE, "Crear una piedra");
        return true;
    }
}
