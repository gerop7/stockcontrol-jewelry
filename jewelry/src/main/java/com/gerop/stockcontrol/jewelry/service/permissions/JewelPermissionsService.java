package com.gerop.stockcontrol.jewelry.service.permissions;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.repository.JewelRepository;
import com.gerop.stockcontrol.jewelry.repository.JewelryStockByInventoryRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

@Service
public class JewelPermissionsService implements IJewelPermissionsService {

    private final UserServiceHelper userServiceHelper;
    private final JewelRepository jewelRepository;
    private final IInventoryPermissionsService invPermissions;
    private final JewelryStockByInventoryRepository stockRepository;

    public JewelPermissionsService(JewelRepository jewelRepository, InventoryPermissionsService invPermissions,
            JewelryStockByInventoryRepository stockRepository, UserServiceHelper userServiceHelper) {
        this.jewelRepository = jewelRepository;
        this.invPermissions = invPermissions;
        this.stockRepository = stockRepository;
        this.userServiceHelper = userServiceHelper;
    }

    @Override
    public boolean canView(Long jewelId, Long inventoryId, Long userId) {
        if(isOwner(jewelId, userId)) return true;
        return stockRepository.existsByJewelIdAndInventoryId(jewelId, inventoryId) && invPermissions.canRead(inventoryId, userId);
    }

    @Override
    public boolean canAddToInventory(Long jewelId, Long ownerId, Long inventoryId) {
        Long currentUserId = userServiceHelper.getCurrentUser().getId();
        return currentUserId.equals(ownerId) && invPermissions.canWrite(inventoryId, currentUserId) && !stockRepository.existsByJewelIdAndInventoryId(jewelId, inventoryId);
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

}
