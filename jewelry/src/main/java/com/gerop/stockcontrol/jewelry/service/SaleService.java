package com.gerop.stockcontrol.jewelry.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.SaleProcessingException;
import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.inventory.InventoryNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.jewel.JewelNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.material.MaterialNotFoundException;
import com.gerop.stockcontrol.jewelry.mapper.SaleMapper;
import com.gerop.stockcontrol.jewelry.model.dto.sale.CompleteSaleDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.JewelSaleWithPendingRestockDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.SaleListDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.SaleResultDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.Sale;
import com.gerop.stockcontrol.jewelry.model.entity.SaleJewel;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.User;
import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryUserPermissionType;
import com.gerop.stockcontrol.jewelry.repository.InventoryRepository;
import com.gerop.stockcontrol.jewelry.repository.SaleRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IJewelService;
import com.gerop.stockcontrol.jewelry.service.interfaces.ISaleService;
import com.gerop.stockcontrol.jewelry.service.permissions.IInventoryPermissionsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService implements ISaleService {
    private final SaleRepository repository;
    private final IJewelService jewelService;
    private final MetalService metalService;
    private final StoneService stoneService;
    private final UserServiceHelper helper;
    private final SaleMapper mapper;
    private final IInventoryPermissionsService invPermissions;
    private final InventoryRepository inventoryRepository;

    @Modifying(clearAutomatically = true)
    @Transactional(noRollbackFor = {
        InvalidQuantityException.class,
        MaterialNotFoundException.class,
        StockNotFoundException.class,
        JewelNotFoundException.class
    })
    @Override
    public SaleResultDto processSale(CompleteSaleDto saleDto) {
        User currentUser = helper.getCurrentUser();
        invPermissions.validatePermission(saleDto.inventoryId(), currentUser.getId(), InventoryUserPermissionType.WRITE,"registrar ventas");

        Inventory inventory = inventoryRepository.findById(saleDto.inventoryId())
            .orElseThrow(() -> new InventoryNotFoundException("No existe el inventario!"));

        if (saleDto.jewels().isEmpty())
            throw new SaleProcessingException("La venta debe incluir al menos una joya");

        Sale sale = new Sale(saleDto.description(), inventory, currentUser);
        List<String> fails = new ArrayList<>();

        for(JewelSaleWithPendingRestockDto saleJewelDto:saleDto.jewels()){
            try {
                SaleJewel saleJewel = jewelService.sale(saleJewelDto.jewelId(),saleJewelDto.quantity(), saleJewelDto.quantityToRestock(), saleJewelDto.total(), inventory); 
                
                sale.getJewels().add(saleJewel);
            } catch (JewelNotFoundException | InvalidQuantityException | StockNotFoundException e) {
                fails.add(e.getMessage());
            }
        }

        saleDto.metals().forEach(
            m -> {
                try {    
                    Metal metal = metalService.findOne(m.metalId())
                        .orElseThrow(() -> new MaterialNotFoundException(m.metalId(),"metal"));
                    if(m.weightUsed()!=null && m.weightUsed()>0)
                        metalService.outflowByWork(metal, inventory, m.weightUsed());
                    if(m.weightToRestock()!=null && m.weightToRestock()>0)
                        metalService.addPendingToRestock(metal, m.weightToRestock(), inventory);
                    if(m.weightReceived()!=null && m.weightReceived()>0)
                        metalService.addStock(metal, inventory, m.weightReceived(), "");
                } catch (MaterialNotFoundException | InvalidQuantityException e) {
                    fails.add(e.getMessage());
                }
            }
        );

        saleDto.stones().forEach(
            s -> {
                try {
                    Stone stone = stoneService.findOne(s.stoneId())
                        .orElseThrow(() -> new MaterialNotFoundException(s.stoneId(),"stone"));
                    if(s.quantityUsed()!=null && s.quantityUsed()>0)
                        stoneService.outflowByWork(stone, inventory, s.quantityUsed());
                    if(s.quantityToRestock()!=null && s.quantityToRestock()>0)
                        stoneService.addPendingToRestock(stone, s.quantityToRestock(), inventory);
                    if(s.quantityReceived()!=null && s.quantityReceived()>0)
                        stoneService.addStock(stone, inventory, s.quantityReceived(), "");
                } catch (MaterialNotFoundException | InvalidQuantityException e) {
                    fails.add(e.getMessage());
                }
            }
        );

        SaleResultDto result = new SaleResultDto(fails, sale);

        if(!sale.getJewels().isEmpty())
            repository.save(sale);

        return result;
    }


    @Override
    @Transactional(readOnly= true)
    public List<SaleListDto> listAll() {
        return repository.findAllByUserIdOrderByTimestampDesc(helper.getCurrentUser().getId()).stream().map(mapper::toListDto).toList();
    }

    @Override
    @Transactional(readOnly= true)
    public List<SaleListDto> listAllFromInventory(Long inventoryId) {
        invPermissions.validatePermission(inventoryId, helper.getCurrentUser().getId(), InventoryUserPermissionType.READ,"ver las ventas");
        return repository.findAllByInventoryIdOrderByTimestampDesc(inventoryId).stream().map(mapper::toListDto).toList();
    }

    @Override
    @Transactional(readOnly= true)
    public List<SaleListDto> listAllAsc() {
        return repository.findAllByUserIdOrderByTimestampAsc(helper.getCurrentUser().getId()).stream().map(mapper::toListDto).toList();
    }

    @Override
    @Transactional(readOnly= true)
    public List<SaleListDto> listAllFromInventoryAsc(Long inventoryId) {
        invPermissions.validatePermission(inventoryId, helper.getCurrentUser().getId(), InventoryUserPermissionType.READ,"ver las ventas");
        return repository.findAllByInventoryIdOrderByTimestampAsc(inventoryId).stream().map(mapper::toListDto).toList();
    }
}
