package com.gerop.stockcontrol.jewelry.service.permissions;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.repository.JewelRepository;
import com.gerop.stockcontrol.jewelry.repository.JewelryStockByInventoryRepository;

@Service
public class JewelPermissionsService implements IJewelPermissionsService {
    private final JewelRepository jewelRepository;
    private final InventoryPermissionsService invPermissions;
    private final JewelryStockByInventoryRepository stockRepository;

    public JewelPermissionsService(JewelRepository jewelRepository, InventoryPermissionsService invPermissions,
            JewelryStockByInventoryRepository stockRepository) {
        this.jewelRepository = jewelRepository;
        this.invPermissions = invPermissions;
        this.stockRepository = stockRepository;
    }

    @Override
    public boolean canView(Long jewelId, Long inventoryId, Long userId) {
        if(isOwner(jewelId, userId)) return true;
        return stockRepository.existsByJewelIdAndInventoryId(jewelId, inventoryId) && invPermissions.canRead(inventoryId, userId);
    }

    @Override
    public boolean canAddToInventory(Long jewelId, Long userId, Long inventoryId) {
        return isOwner(jewelId, userId) && !stockRepository.existsByJewelIdAndInventoryId(jewelId, inventoryId);
    }
    
    @Override
    public boolean canModifyStock(Long jewelId, Long inventoryId, Long userId) {
        return stockRepository.existsByJewelIdAndInventoryId(jewelId, inventoryId) && (isOwner(jewelId, userId) ||invPermissions.canWrite(inventoryId, userId));
    }

    @Override
    public boolean canEditInfo(Long jewelId, Long userId) {
        return isOwner(jewelId, userId);
    }

    @Override
    public boolean isOwner(Long jewelId, Long userId) {
        return jewelRepository.existsByIdAndUserId(jewelId,userId);
    }
}
