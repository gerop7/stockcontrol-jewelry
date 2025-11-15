package com.gerop.stockcontrol.jewelry.service;

import com.gerop.stockcontrol.jewelry.mapper.MaterialMapper;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingMetalRestock;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.MetalStockByInventory;

import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.movement.MetalMovementService;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.PendingMetalRestockService;
import com.gerop.stockcontrol.jewelry.service.permissions.MetalPermissionsService;
import com.gerop.stockcontrol.jewelry.service.stockperinventory.MetalStockByInventoryService;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.dto.materials.MetalDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;

import com.gerop.stockcontrol.jewelry.model.entity.movement.MetalMovement;
import com.gerop.stockcontrol.jewelry.repository.MetalRepository;

import java.util.function.Function;

@Service
public class MetalService extends AbstractMaterialService<Metal, Float, MetalDto, MetalRepository, MetalMovement, PendingMetalRestock, MetalStockByInventory> {
    public MetalService(MetalRepository repository, UserServiceHelper helperService, MetalPermissionsService permissionsService, MetalMovementService movementService, PendingMetalRestockService pendingRestockService, IInventoryService inventoryService, MaterialMapper mapper, MetalStockByInventoryService stockService) {
        super(repository, helperService, permissionsService, movementService, pendingRestockService, inventoryService, mapper, stockService, dto -> new Metal(dto.name(), null, false, dto.url()));
    }

    @Override
    protected void createInitialInventoryAndRestock(Metal material, MetalDto metalDto) {
        metalDto.stockByInventory().forEach(
                stock -> {
                    validateCanCreate(stock.inventoryId());

                    Inventory inventory = getInventoryOrThrow(stock.inventoryId());

                    if(stock.weight()!=null && stock.weight()>=0)
                        material.getStockByInventory().add(stockService.create(material, inventory, stock.weight()));

                    material.getPendingMetalRestock().add(pendingRestockService.create(material, inventory));
                }
        );

    }

    @Override
    protected String className() {
        return "Metal";
    }

    @Override
    protected void addToInventoryInternal(Metal material, Inventory inventory, Float quantity) {

    }

    @Override
    protected void removeFromInventoryInternal(Metal material, Inventory inventory) {

    }
}
