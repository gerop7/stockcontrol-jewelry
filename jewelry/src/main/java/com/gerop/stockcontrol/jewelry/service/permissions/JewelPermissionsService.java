package com.gerop.stockcontrol.jewelry.service.permissions;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.exception.material.MaterialPermissionDeniedException;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryUserPermissionType;
import com.gerop.stockcontrol.jewelry.repository.JewelRepository;
import com.gerop.stockcontrol.jewelry.repository.JewelryStockByInventoryRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JewelPermissionsService implements IJewelPermissionsService {

    private final UserServiceHelper userServiceHelper;
    private final JewelRepository jewelRepository;
    private final IInventoryPermissionsService invPermissions;
    private final JewelryStockByInventoryRepository stockRepository;
    private final IMaterialPermissionsService<Metal> metalPermissions;
    private final IMaterialPermissionsService<Stone> stonePermissions;


    @Override
    public boolean canView(Long jewelId, Long inventoryId, Long userId) {
        if(isOwner(jewelId, userId)) 
            return true;
        return stockRepository.existsByJewelIdAndInventoryId(jewelId, inventoryId) && invPermissions.canRead(inventoryId, userId);
    }

    @Override
    public boolean canAddToInventory(Long jewelId, Long ownerId, Long inventoryId) {
        Long currentUserId = userServiceHelper.getCurrentUser().getId();
        return currentUserId.equals(ownerId) && invPermissions.canWrite(inventoryId, currentUserId);
    }

    @Override
    public boolean canRemoveFromInventory(Long jewelId, Long ownerId, Long inventoryId) {
        return isOwner(jewelId, ownerId) && invPermissions.canWrite(inventoryId, ownerId);
    }

    @Override
    public boolean canModifyStock(Long jewelId, Long inventoryId, Long userId) {
        return stockRepository.existsByJewelIdAndInventoryId(jewelId, inventoryId) && invPermissions.canWrite(inventoryId, userId);
    }

    @Override
    public boolean canEditInfo(Long jewelId, Long userId) {
        return isOwner(jewelId, userId);
    }

    @Override
    public boolean isOwner(Long jewelId, Long userId) {
        return jewelRepository.existsByIdAndUserId(jewelId,userId);
    }

    @Override
    public boolean canDelete(Long jewelId) {
        return isOwner(jewelId, userServiceHelper.getCurrentUser().getId());
    }

    @Override
    public boolean canCreate(Long inventoryId, Long userId, Set<Long> metalIds, Set<Long> stoneIds) {
        invPermissions.validatePermission(inventoryId, userId, InventoryUserPermissionType.WRITE, "Crear una joya");

        metalIds.forEach(
            mId -> {
                if(!metalPermissions.canUseToCreateWithoutInvPermission(mId, userId, inventoryId))
                    throw new MaterialPermissionDeniedException("No tienes permisos para usar el metal con ID: "+mId+", en el Inventario con ID: "+inventoryId+".");
            }
        );
        stoneIds.forEach(
            sId -> {
                if(!stonePermissions.canUseToCreateWithoutInvPermission(sId, userId, inventoryId))
                    throw new MaterialPermissionDeniedException("No tienes permisos para usar la piedra con ID: "+sId+", en el Inventario con ID: "+inventoryId+".");
            }
        );

        return true;
    }

}
