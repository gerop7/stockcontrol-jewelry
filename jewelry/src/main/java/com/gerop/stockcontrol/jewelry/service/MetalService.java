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
import com.gerop.stockcontrol.jewelry.model.dto.MetalDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateMaterialDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.movement.MetalMovement;
import com.gerop.stockcontrol.jewelry.repository.InventoryRepository;
import com.gerop.stockcontrol.jewelry.repository.MetalRepository;
import com.gerop.stockcontrol.jewelry.repository.MetalStockByInventoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMaterialService;
import com.gerop.stockcontrol.jewelry.service.movement.IMaterialMovementService;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.PendingMetalRestockService;
import com.gerop.stockcontrol.jewelry.service.permissions.IMaterialPermissionsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetalService implements IMaterialService<Metal, Float, MetalDto> {
    private final MetalRepository repository;
    private final MetalStockByInventoryRepository stockRepository;
    private final UserServiceHelper helperService;
    private final IMaterialPermissionsService<Metal> metalPermissionsService;
    private final IMaterialMovementService<MetalMovement,Metal,Float> movementService;
    private final PendingMetalRestockService pendingRestockService;
    private final InventoryRepository inventoryRepository;

    @Override
    public MetalDto create(MetalDto material) {
        
    }

    @Override
    public MetalDto save(Metal material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Long materialId, UpdateMaterialDataDto data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addStock(Long materialid, Long inventoryId, Float quantity, String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MetalDto sale(Long materialid, Long inventoryId, Float quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void outflowByWork(Long materialid, Long inventoryId, Float quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void outflowByWork(Metal material, Inventory inventory, Float quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    @Transactional
    public void addPendingToRestock(Metal metal, Float quantity, Inventory inventory) {
        if(!metalPermissionsService.canUpdateStock(metal.getId(), helperService.getCurrentUser().getId(), inventory.getId()))
            throw new InventoryAccessDeniedException("No tienes permiso para modificar stock pendiente de reposición!");

        handleAddPendingToRestock(metal, quantity, inventory);

        movementService.marked_replacement(metal, quantity, inventory);
    }

    @Override
    @Transactional
    public void removePendingToRestock(Metal material, Float quantity, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    @Transactional
    public void addPendingToRestock(Long materialId, Float quantity, Inventory inventory) {
        Metal metal = repository.findById(materialId)
            .orElseThrow(()-> new MaterialNotFoundException(materialId,"Metal"));

        addPendingToRestock(metal, quantity, inventory);
    }

    @Transactional
    public void handleAddPendingToRestock(Metal metal, Float quantity, Inventory inventory) {
        
        Objects.requireNonNull(metal, "Metal cannot be null");
        Objects.requireNonNull(inventory, "Inventory cannot be null");
        if (quantity == null || quantity <= 0) 
            throw new InvalidQuantityException("La cantidad de "+metal.getName()+" a reponer es invalida!");

        pendingRestockService.findByMetalIdAndInventoryId(metal.getId(), inventory.getId())
            .ifPresentOrElse(
                r -> pendingRestockService.addToRestock(r, quantity),
                () -> metal.getPendingMetalRestock().add(
                    pendingRestockService.createSave(metal, inventory, quantity))
        );
    }

    @Override
    public void removePendingToRestock(Long materialId, Float quantity, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addToInventory(Long materialid, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canUseToCreate(Long materialId, Long userId, Long inventoryId) {
        throw new UnsupportedOperationException("Unimplemented method 'canUseToCreate'");
    }

    @Override
    public boolean canAddToInventory(Long materialId, Long userId, Long inventoryId) {
        throw new UnsupportedOperationException("Unimplemented method 'canAddToInventory'");
    }

    @Override
    public void removeFromInventory(Long materialid, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Metal> findOne(Long materialId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Metal> findAllByIds(Set<Long> materialIds) {
        throw new UnsupportedOperationException("Unimplemented method 'findAllByIds'");
    }

    

    



}
