package com.gerop.stockcontrol.jewelry.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.StockNotAvailableException;
import com.gerop.stockcontrol.jewelry.mapper.SaleMapper;
import com.gerop.stockcontrol.jewelry.model.dto.MetalWeightDto;
import com.gerop.stockcontrol.jewelry.model.dto.StoneQuantityDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.CompleteSaleDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.JewelSaleWithPendingRestockDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.SaleListDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.SaleResultDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Sale;
import com.gerop.stockcontrol.jewelry.model.entity.SaleJewel;
import com.gerop.stockcontrol.jewelry.model.entity.User;
import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryUserPermissionType;
import com.gerop.stockcontrol.jewelry.repository.InventoryRepository;
import com.gerop.stockcontrol.jewelry.repository.SaleRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IJewelService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMetalService;
import com.gerop.stockcontrol.jewelry.service.interfaces.ISaleService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IStoneService;
import com.gerop.stockcontrol.jewelry.service.permissions.IInventoryPermissionsService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SaleService implements ISaleService {
    private final SaleRepository repository;
    private final IJewelService jewelService;
    private final IMetalService metalService;
    private final IStoneService stoneService;
    private final UserServiceHelper helper;
    private final SaleMapper mapper;
    private final IInventoryPermissionsService invPermissions;
    private final InventoryRepository inventoryRepository;

    

    public SaleService(SaleRepository repository, IJewelService jewelService, IMetalService metalService,
            IStoneService stoneService, UserServiceHelper helper, SaleMapper mapper,
            IInventoryPermissionsService invPermissions, InventoryRepository inventoryRepository) {
        this.repository = repository;
        this.jewelService = jewelService;
        this.metalService = metalService;
        this.stoneService = stoneService;
        this.helper = helper;
        this.mapper = mapper;
        this.invPermissions = invPermissions;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    @Override
    public SaleResultDto processSale(CompleteSaleDto saleDto) {
        User currentUser = helper.getCurrentUser();
        invPermissions.validatePermission(saleDto.inventoryId(), currentUser.getId(), InventoryUserPermissionType.WRITE,"registrar ventas");

        Inventory inventory = inventoryRepository.findById(saleDto.inventoryId())
            .orElseThrow(() -> new EntityNotFoundException("No existe el inventario!"));

        if (saleDto.jewels().isEmpty())
            throw new IllegalArgumentException("La venta debe incluir al menos una joya");

        Sale sale = new Sale(saleDto.description(), inventory, currentUser);
        List<String> fails = new ArrayList<>();

        for(JewelSaleWithPendingRestockDto saleJewelDto:saleDto.jewels()){

            try {
                SaleJewel saleJewel = jewelService.sale(saleJewelDto.jewelId(), saleJewelDto.quantity(), 
                    saleJewelDto.quantityToRestock(), saleJewelDto.total(), inventory.getId()); 

                for(MetalWeightDto metalWeightDto:saleJewelDto.metalToRestock()){
                    try {
                        metalService.addPendingToRestock(metalWeightDto.metalId(), metalWeightDto.weight(), inventory.getId(), saleJewelDto.jewelId());
                    } catch (EntityNotFoundException | SecurityException e) {
                        fails.add(e.getMessage());
                    }
                }

                if(jewelService.haveStones(saleJewelDto.jewelId())){
                    for(StoneQuantityDto stoneQuantityDto:saleJewelDto.stoneToRestock()){
                        try {
                            stoneService.addPendingToRestock(stoneQuantityDto.stoneId(), stoneQuantityDto.quantity(), inventory.getId(), saleJewelDto.jewelId());
                        } catch (EntityNotFoundException | SecurityException e) {
                            fails.add(e.getMessage());
                        }
                    }
                }
                
                sale.getJewels().add(saleJewel);
            } catch (StockNotAvailableException | EntityNotFoundException | SecurityException e) {
                fails.add(e.getMessage());
            }
        }

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
