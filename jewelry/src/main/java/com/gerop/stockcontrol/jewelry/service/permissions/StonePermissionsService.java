package com.gerop.stockcontrol.jewelry.service.permissions;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.repository.StoneRepository;
import com.gerop.stockcontrol.jewelry.repository.StoneStockByInventoryRepository;

@Service
public class StonePermissionsService implements IMaterialPermissionsService<Stone> {

    private final StoneRepository stoneRepository;
    private final IInventoryPermissionsService invPermissions;
    private final StoneStockByInventoryRepository stoneStockRepository;

    public StonePermissionsService(IInventoryPermissionsService invPermissions, StoneRepository stoneRepository, StoneStockByInventoryRepository stoneStockRepository) {
        this.invPermissions = invPermissions;
        this.stoneRepository = stoneRepository;
        this.stoneStockRepository = stoneStockRepository;
    }

    @Override
    public boolean isOwner(Long materialId, Long userId) {
        return stoneRepository.existsByIdAndUserId(materialId, userId);
    }

    @Override
    public boolean canUseToCreate(Long materialId, Long userId, Long inventoryId) {
        if(!stoneStockRepository.existsBy)

        return 
    }

    @Override
    public boolean canUpdateStockByInventory(Long materialId, Long userId, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
