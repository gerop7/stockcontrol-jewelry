package com.gerop.stockcontrol.jewelry.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.dto.MetalDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateMaterialDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMaterialService;

@Service
public class MetalService implements IMaterialService<Metal, Float, MetalDto> {

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
    public MetalDto addStock(Long materialid, Long inventoryId, Float quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MetalDto sale(Long materialid, Long inventoryId, Float quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPendingToRestock(Long materialId, Float quantity, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePendingToRestock(Long materialId, Float quantity, Long inventoryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Metal> findOne(Long materialId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
