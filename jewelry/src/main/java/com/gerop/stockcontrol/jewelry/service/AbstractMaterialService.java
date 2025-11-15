package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.gerop.stockcontrol.jewelry.model.dto.materials.MaterialDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Material;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMaterialService;

public class AbstractMaterialService<M extends Material, N extends Number, MDto extends MaterialDto> implements IMaterialService<M, N, MDto> {

    @Override
    public MDto create(MDto material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(Long materialId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(M material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public M save(M material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(MDto data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addStock(Long materialId, Long inventoryId, N quantity, String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addStock(M material, Inventory inventory, N quantity, String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void outflowByWork(Long materialId, Long inventoryId, N quantity, N quantityToRestock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void outflowByWork(M material, Inventory inventory, N quantity, N quantityToRestock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MDto sale(Long materialId, Long inventoryId, N quantity, Float total, N quantityToRestock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addToInventory(Long materialId, Inventory inventory, N quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeFromInventory(Long materialId, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPendingToRestock(Long materialId, N quantity, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPendingToRestock(M material, N quantity, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePendingToRestock(Long materialId, N quantity, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePendingToRestock(M material, N quantity, Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<M> findOne(Long materialId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<M> findAllByIds(Set<Long> materialIds) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected boolean canUseToCreate(Long materialId, Long userId, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected boolean canAddToInventory(Long materialId, Long userId, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
