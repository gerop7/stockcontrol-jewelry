package com.gerop.stockcontrol.jewelry.service;

import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
import com.gerop.stockcontrol.jewelry.mapper.MaterialMapper;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingStoneRestock;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.StoneStockByInventory;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.movement.StoneMovementService;
import com.gerop.stockcontrol.jewelry.service.permissions.StonePermissionsService;
import com.gerop.stockcontrol.jewelry.service.stockperinventory.StoneStockByInventoryService;
import org.springframework.stereotype.Service;
import com.gerop.stockcontrol.jewelry.model.dto.materials.StoneDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.movement.StoneMovement;
import com.gerop.stockcontrol.jewelry.repository.StoneRepository;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.PendingStoneRestockService;


@Service
public class StoneService extends AbstractMaterialService<Stone, Long, StoneDto, StoneRepository, StoneMovement, PendingStoneRestock, StoneStockByInventory> {
    public StoneService(StoneRepository repository, UserServiceHelper helperService, StonePermissionsService permissionsService,
                        StoneMovementService movementService, PendingStoneRestockService pendingRestockService, IInventoryService inventoryService,
                        MaterialMapper mapper, StoneStockByInventoryService stockService) {
        super(repository, helperService, permissionsService, movementService, pendingRestockService, inventoryService, mapper, stockService, dto -> new Stone(dto.name(), null, false, dto.url()));
    }

    @Override
    protected void createInitialInventoryAndRestock(Stone material, StoneDto stoneDto) {
        stoneDto.stockByInventory().forEach(
                stock -> {
                    validateCanCreate(stock.inventoryId());

                    Inventory inventory = getInventoryOrThrow(stock.inventoryId());

                    if(stock.stock()!=null && stock.stock()>=0){
                        addToInventoryInternal(material, inventory, stock.stock());
                        material.getPendingStoneRestock().add(pendingRestockService.createSave(material, inventory));
                    }
                }
        );
    }

    @Override
    protected String className() {
        return "Piedra";
    }

    @Override
    protected void addToInventoryInternal(Stone material, Inventory inventory, Long quantity) {
        material.getStockByInventory().add(stockService.create(material, inventory, quantity));
    }

    @Override
    protected void removeFromInventoryInternal(Stone material, Inventory inventory) {
        StoneStockByInventory stock = stockService.findOne(material, inventory)
                .orElseThrow(() -> new StockNotFoundException("No existe stock de "+material.getName()+" en el inventario "+inventory.getName()+"."));

        stockService.remove(stock);
        material.getStockByInventory().remove(stock);
    }
}
