package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.inventory.InventoryAccessDeniedException;
import com.gerop.stockcontrol.jewelry.exception.material.MaterialNotFoundException;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateMaterialDataDto;
import com.gerop.stockcontrol.jewelry.model.dto.materials.StoneDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.movement.StoneMovement;
import com.gerop.stockcontrol.jewelry.repository.InventoryRepository;
import com.gerop.stockcontrol.jewelry.repository.StoneRepository;
import com.gerop.stockcontrol.jewelry.repository.StoneStockByInventoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMaterialService;
import com.gerop.stockcontrol.jewelry.service.movement.IMaterialMovementService;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.PendingStoneRestockService;
import com.gerop.stockcontrol.jewelry.service.permissions.IMaterialPermissionsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoneService implements IMaterialService<Stone, Long, StoneDto> {

    private final StoneRepository repository;
    private final StoneStockByInventoryRepository stockRepository;
    private final UserServiceHelper helperService;
    private final IMaterialPermissionsService<Stone> stonePermissionsService;
    private final PendingStoneRestockService pendingRestockService;
    private final InventoryRepository inventoryRepository;
    private final IMaterialMovementService<StoneMovement,Stone,Long> movementService;

    @Override
    public StoneDto create(StoneDto material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StoneDto save(Stone material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    @Override
    public void update(StoneDto data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addStock(Long materialid, Long inventoryId, Long quantity, String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void addStock(Stone material, Inventory inventory, Long quantity, String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public void outflowByWork(Long materialid, Long inventoryId, Long quantity, Long quantityToRestock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void outflowByWork(Stone material, Inventory inventory, Long quantity, Long quantityToRestock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StoneDto sale(Long materialid, Long inventoryId, Long quantity, Float total, Long quantityToRestock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional
    @Override
    public void addPendingToRestock(Long materialId, Long quantity, Long inventoryId) {
        Stone stone = repository.findById(materialId)
            .orElseThrow(()-> new MaterialNotFoundException(materialId,"Stone"));

        
        addPendingToRestock(stone, quantity, inventory);
    }

    @Override
    @Transactional
    public void removePendingToRestock(Long materialId, Long quantity, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public void addPendingToRestock(Stone stone, Long quantity, Inventory inventory) {
        if(!stonePermissionsService.canUpdateStock(stone.getId(), helperService.getCurrentUser().getId(), inventory.getId()))
            throw new InventoryAccessDeniedException("No tienes permiso para modificar stock pendiente de reposición!");
                
        handleAddPendingToRestock(stone, quantity, inventory);

        movementService.marked_replacement(stone, quantity, inventory);
    }

    @Override
    @Transactional
    public void removePendingToRestock(Stone material, Long quantity, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional
    public void handleAddPendingToRestock(Stone stone, Long quantity, Inventory inventory) {
        Objects.requireNonNull(stone, "Stone cannot be null");
        Objects.requireNonNull(inventory, "Inventory cannot be null");
        if (quantity == null || quantity <= 0) 
            throw new InvalidQuantityException("La cantidad de "+stone.getName()+" a reponer es invalida!");
        
        pendingRestockService.findByStoneIdAndInventoryId(stone.getId(), inventory.getId())
            .ifPresentOrElse(
                r -> pendingRestockService.addToRestock(r, quantity),
                () -> stone.getPendingStoneRestock().add(
                    pendingRestockService.createSave(stone, inventory, quantity))
        );
    }

    @Override
    public void addToInventory(Long materialid, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeFromInventory(Long materialid, Inventory inventory) {
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
    public boolean delete(Long materialId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(Stone material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
