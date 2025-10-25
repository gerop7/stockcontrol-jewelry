package com.gerop.stockcontrol.jewelry.service;

import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.dto.StoneDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateMaterialDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMaterialService;

public class StoneService implements IMaterialService<Stone, Long, StoneDto> {

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
    public StoneDto addStock(Long materialid, Long inventoryId, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StoneDto sale(Long materialid, Long inventoryId, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPendingToRestock(Long materialId, Long quantity, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePendingToRestock(Long materialId, Long quantity, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Stone> findOne(Long materialId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
