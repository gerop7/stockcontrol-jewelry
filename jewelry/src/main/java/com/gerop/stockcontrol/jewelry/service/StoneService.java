package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.dto.StoneDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateMaterialDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.repository.InventoryRepository;
import com.gerop.stockcontrol.jewelry.repository.StoneRepository;
import com.gerop.stockcontrol.jewelry.repository.StoneStockByInventoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMaterialService;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.PendingStoneRestockService;
import com.gerop.stockcontrol.jewelry.service.permissions.IMaterialPermissionsService;

import jakarta.persistence.EntityNotFoundException;

public class StoneService implements IMaterialService<Stone, Long, StoneDto> {

    private final StoneRepository repository;
    private final StoneStockByInventoryRepository stockRepository;
    private final UserServiceHelper helperService;
    private final IMaterialPermissionsService<Stone> stonePermissionsService;
    private final PendingStoneRestockService pendingRestockService;
    private final InventoryRepository inventoryRepository;

    public StoneService(UserServiceHelper helperService, InventoryRepository inventoryRepository, PendingStoneRestockService pendingRestockService, StoneRepository repository, StoneStockByInventoryRepository stockRepository, IMaterialPermissionsService<Stone> stonePermissionsService) {
        this.helperService = helperService;
        this.inventoryRepository = inventoryRepository;
        this.pendingRestockService = pendingRestockService;
        this.repository = repository;
        this.stockRepository = stockRepository;
        this.stonePermissionsService = stonePermissionsService;
    }

    
    @Override
    public Stone create(StoneDto material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Stone save(Stone material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StoneDto update(Long materialId, UpdateMaterialDataDto data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StoneDto addStock(Long materialid, Long inventoryId, Long quantity, String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StoneDto sale(Long materialid, Long inventoryId, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional
    @Override
    public void addPendingToRestock(Long materialId, Long quantity, Inventory inventory) {
        if(!stonePermissionsService.canUpdateStock(materialId, helperService.getCurrentUser().getId(), inventory.getId()))
            throw new SecurityException("No tienes permiso para modificar stock pendiente de reposición!");
        
        Stone stone = repository.findById(materialId)
            .orElseThrow(()-> new EntityNotFoundException("Piedra no encontrada!"));

        handleAddPendingToRestock(stone, quantity, inventory);
    }

    @Transactional
    public void handleAddPendingToRestock(Stone stone, Long quantity, Inventory inventory) {
        Objects.requireNonNull(stone, "Stone cannot be null");
        Objects.requireNonNull(inventory, "Inventory cannot be null");
        if (quantity == null || quantity <= 0) return;
        
        pendingRestockService.findByStoneIdAndInventoryId(stone.getId(), inventory.getId())
            .ifPresentOrElse(
                r -> pendingRestockService.addToRestock(r, quantity),
                () -> stone.getPendingStoneRestock().add(
                    pendingRestockService.createSave(stone, inventory, quantity))
        );
    }

    @Override
    public void removePendingToRestock(Long materialId, Long quantity, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Stone> findOne(Long materialId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Stone> findAllByIds(Set<Long> materialIds) {
        throw new UnsupportedOperationException("Unimplemented method 'findAllByIds'");
    }

    @Override
    public boolean canUseToCreate(Long materialId, Long userId, Long inventoryId) {
        throw new UnsupportedOperationException("Unimplemented method 'canUseToCreate'");
    }

    @Override
    public boolean canAddToInventory(Long materialId, Long userId, Long inventoryId) {
        throw new UnsupportedOperationException("Unimplemented method 'canAddToInventory'");
    }


}
