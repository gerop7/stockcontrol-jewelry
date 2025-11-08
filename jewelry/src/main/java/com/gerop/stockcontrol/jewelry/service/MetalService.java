package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.inventory.InventoryAccessDeniedException;
import com.gerop.stockcontrol.jewelry.exception.material.MaterialNotFoundException;
import com.gerop.stockcontrol.jewelry.model.dto.MetalDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateMaterialDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.repository.InventoryRepository;
import com.gerop.stockcontrol.jewelry.repository.MetalRepository;
import com.gerop.stockcontrol.jewelry.repository.MetalStockByInventoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMaterialService;
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
    private final PendingMetalRestockService pendingRestockService;
    private final InventoryRepository inventoryRepository;

    @Override
    public Metal create(MetalDto material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Metal save(Metal material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MetalDto update(Long materialId, UpdateMaterialDataDto data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MetalDto addStock(Long materialid, Long inventoryId, Float quantity, String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MetalDto sale(Long materialid, Long inventoryId, Float quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public void addPendingToRestock(Long materialId, Float quantity, Inventory inventory) {
        if(!metalPermissionsService.canUpdateStock(materialId, helperService.getCurrentUser().getId(), inventory.getId()))
            throw new InventoryAccessDeniedException("No tienes permiso para modificar stock pendiente de reposición!");
        
        Metal metal = repository.findById(materialId)
            .orElseThrow(()-> new MaterialNotFoundException(materialId,"Metal"));

        handleAddPendingToRestock(metal, quantity, inventory);
    }

    @Transactional
    public void handleAddPendingToRestock(Metal metal, Float quantity, Inventory inventory) {
        
        Objects.requireNonNull(metal, "Metal cannot be null");
        Objects.requireNonNull(inventory, "Inventory cannot be null");
        if (quantity == null || quantity <= 0) return;

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
    public Optional<Metal> findOne(Long materialId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Metal> findAllByIds(Set<Long> materialIds) {
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
