package com.gerop.stockcontrol.jewelry.service;

import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
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

                    if(stock.weight()!=null && stock.weight()>=0){
                        addToInventoryInternal(material, inventory, stock.weight());
                        material.getPendingMetalRestock().add(pendingRestockService.createSave(material, inventory));
                    }
                }
        );
    }

    @Override
    protected String className() {
        return "Metal";
    }

    @Override
    protected void addToInventoryInternal(Metal material, Inventory inventory, Float quantity) {
        material.getStockByInventory().add(stockService.create(material, inventory, quantity));
    }

    @Override
    protected void removeFromInventoryInternal(Metal material, Inventory inventory) {
        MetalStockByInventory stock = stockService.findOne(material, inventory)
                .orElseThrow(() -> new StockNotFoundException("No existe stock de "+material.getName()+" en el inventario "+inventory.getName()+"."));

        stockService.remove(stock);
        material.getStockByInventory().remove(stock);
    }

    @Override
    protected void filterRelationsByInventory(Metal metal, Long inventoryId) {
        metal.setStockByInventory(
                metal.getStockByInventory().stream()
                        .filter(s -> s.getInventory().getId().equals(inventoryId))
                        .toList()
        );

        metal.setPendingMetalRestock(
                metal.getPendingMetalRestock().stream()
                        .filter(p -> p.getInventory().getId().equals(inventoryId))
                        .toList()
        );
    }
}
