package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.material.MaterialPermissionDeniedException;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.MetalStockByInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.inventory.InventoryNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.material.MaterialNotFoundException;
import com.gerop.stockcontrol.jewelry.mapper.MaterialMapper;
import com.gerop.stockcontrol.jewelry.model.dto.materials.MetalDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.User;
import com.gerop.stockcontrol.jewelry.model.entity.movement.MetalMovement;
import com.gerop.stockcontrol.jewelry.repository.MetalRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMaterialService;
import com.gerop.stockcontrol.jewelry.service.movement.IMaterialMovementService;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.PendingMetalRestockService;
import com.gerop.stockcontrol.jewelry.service.permissions.IMaterialPermissionsService;
import com.gerop.stockcontrol.jewelry.service.stockperinventory.MetalStockByInventoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetalService implements IMaterialService<Metal, Float, MetalDto> {
    private final MetalRepository repository;
    private final UserServiceHelper helperService;
    private final IMaterialPermissionsService<Metal> metalPermissionsService;
    private final IMaterialMovementService<MetalMovement,Metal,Float> movementService;
    private final PendingMetalRestockService pendingRestockService;
    private final IInventoryService inventoryService;
    private final MaterialMapper mapper;
    private final MetalStockByInventoryService stockService;

    @Override
    @Transactional
    public MetalDto create(MetalDto metalDto) {
        User currentUser = helperService.getCurrentUser();
        Metal metal = new Metal(metalDto.name(),currentUser,false, metalDto.url());
        save(metal);

        metalDto.stockByInventory().forEach(
            stock -> {
                metalPermissionsService.canCreate(stock.inventoryId());

                Inventory inventory = inventoryService.findOne(stock.inventoryId())
                    .orElseThrow(()-> new InventoryNotFoundException(stock.inventoryId()));

                if(stock.weight()!=null && stock.weight()>=0)
                    metal.getStockByInventory().add(stockService.create(metal, inventory, stock.weight()));
                
                metal.getPendingMetalRestock().add(pendingRestockService.create(metal, inventory));
            }
        );
        
        return (MetalDto)mapper.toDto(save(metal));
    }

    @Override
    @Transactional
    public Metal save(Metal metal) {
        return repository.save(metal);
    }

    protected String className(){
        return "Metal";
    }
    
    @Override
    @Transactional
    public boolean delete(Long metalId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public boolean delete(Metal metal) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public void update(MetalDto data) {
        if(data==null)
            throw new RequiredFieldException("No hay informacion para actualizar!");
        Metal metal = repository.findById(data.id())
                .orElseThrow(() -> new MaterialNotFoundException("El "+className()+" ID: "+data.id()+" No existe!"));
        if(!metalPermissionsService.isOwner(metal.getId(), helperService.getCurrentUser().getId()))
            throw new MaterialPermissionDeniedException("No puedes modificar la información de "+className()+ ".");

        boolean updated =false;

        if(data.url()!=null){
            metal.setUrlImage(data.url());
            updated =true;
        }

        if(data.name()!=null){
            metal.setName(data.name());
            updated =true;
        }

        if(updated)
            save(metal);
    }

    protected void validateFields(Metal metal, Inventory inventory, Float quantity){
        if(metal==null || inventory==null)
            throw new RequiredFieldException("Debes completar todos los campos!");
        validateQuantity(quantity);
    }

    protected void validateQuantity(Float quantity){
        if(quantity==null || quantity<0)
            throw new InvalidQuantityException("Las cantidades no pueden ser negativas!");
    }

    @Override
    @Transactional
    public void addStock(Long metalId, Long inventoryId, Float quantity, String description) {
        Inventory inventory = inventoryService.findOne(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
        Metal metal = findOne(metalId)
                .orElseThrow(() -> new MaterialNotFoundException(metalId,className()));

        addStock(metal,inventory,quantity,description);
    }

    @Override
    @Transactional
    public void addStock(Metal metal, Inventory inventory, Float quantity, String description) {
        validateUpdateStock(metal, inventory);

        stockService.addStock(metal, inventory, quantity);
        pendingRestockService.removeFromRestock(metal,inventory,quantity);
        movementService.inflow(quantity, metal, inventory);

        save(metal);
    }

    protected void validateUpdateStock(Metal object, Inventory inventory){
        if(metalPermissionsService.canUpdateStock(object.getId(), helperService.getCurrentUser().getId(), inventory.getId()))
            throw new MaterialPermissionDeniedException("No tienes permiso para cambiar el Stock de "+className()+" en el Inventario "+inventory.getName()+".");
    }

    @Transactional
    protected void outflow(Metal metal, Inventory inventory, Float quantity, Float quantityToRestock){
        validateUpdateStock(metal, inventory);

        stockService.removeStock(metal, inventory, quantity);
        if(quantityToRestock>0)
            pendingRestockService.removeFromRestock(metal,inventory,quantityToRestock);
    }

    @Override
    @Transactional
    public MetalDto sale(Long metalId, Long inventoryId, Float quantity, Float total, Float quantityToRestock) {
        Inventory inventory = inventoryService.findOne(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
        Metal metal = findOne(metalId)
                .orElseThrow(() -> new MaterialNotFoundException(metalId,className()));

        outflow(metal,inventory,quantity,total);

        movementService.sale(metal,inventory,quantity,total);
        return (MetalDto) mapper.toDto(save(metal));
    }

    @Override
    @Transactional
    public void outflowByWork(Long metalId, Long inventoryId, Float quantity, Float quantityToRestock) {
        Inventory inventory = inventoryService.findOne(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
        Metal metal = findOne(metalId)
                .orElseThrow(() -> new MaterialNotFoundException(metalId,className()));

        outflowByWork(metal, inventory, quantity, quantityToRestock);
    }

    @Override
    @Transactional
    public void outflowByWork(Metal metal, Inventory inventory, Float quantity, Float quantityToRestock) {
        outflow(metal, inventory, quantity, quantityToRestock);

        movementService.outflow(quantity,metal, inventory);

        save(metal);
    }


    @Override
    @Transactional
    public void addPendingToRestock(Metal metal, Float quantity, Inventory inventory) {
        validateUpdateStock(metal,inventory);

        pendingRestockService.addToRestock(metal, inventory, quantity);
    }

    @Override
    @Transactional
    public void removePendingToRestock(Metal metal, Float quantity, Inventory inventory) {
        validateUpdateStock(metal,inventory);

        pendingRestockService.removeFromRestock(metal,inventory,quantity);
    }


    @Override
    @Transactional
    public void addPendingToRestock(Long metalId, Float quantity, Long inventoryId) {
        Metal metal = repository.findById(metalId)
                .orElseThrow(()-> new MaterialNotFoundException(metalId,className()));
        Inventory inventory = inventoryService.findOne(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        addPendingToRestock(metal, quantity, inventory);
    }

    @Override
    @Transactional
    public void removePendingToRestock(Long metalId, Float quantity, Long inventoryId) {
        Metal metal = repository.findById(metalId)
                .orElseThrow(()-> new MaterialNotFoundException(metalId,className()));
        Inventory inventory = inventoryService.findOne(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        removePendingToRestock(metal, quantity, inventory);
    }

    @Override
    @Transactional
    public void addToInventory(Long metalId, Inventory inventory, Float quantity) {
        Metal metal = repository.findByIdWithStockByInventory(metalId)
                .orElseThrow(()-> new MaterialNotFoundException(metalId,className()));

        if(metalPermissionsService.canAddToInventory(metalId, helperService.getCurrentUser().getId(), inventory.getId()))
            throw new MaterialPermissionDeniedException("No puedes añadir el "+getClass()+" "+metal.getName()+" en el Inventario "+inventory.getName()+".");

        metal.getStockByInventory().add(stockService.create(metal, inventory, quantity));

        save(metal);
    }

    @Override
    public void removeFromInventory(Long metalId, Inventory inventory) {
        Metal metal = repository.findByIdWithStockByInventory(metalId)
                .orElseThrow(()-> new MaterialNotFoundException(metalId,className()));

        if(metalPermissionsService.canDeleteFromInventory(metalId, inventory.getId()))
            throw new MaterialPermissionDeniedException("No puedes eliminar el "+getClass()+" "+metal.getName()+" en el Inventario "+inventory.getName()+".");

        MetalStockByInventory stock = metal.getStockByInventory().stream().filter(p -> p.getInventory().getId().equals(inventory.getId())).findFirst()
                        .orElseThrow(() -> new StockNotFoundException("No existe stock de "+metal.getName()+" en el inventario "+inventory.getName()+"."));

        metal.getStockByInventory().remove(stock);
        stockService.remove(stock);

        save(metal);
    }

    @Override
    public Optional<Metal> findOne(Long metalId) {
        return repository.findByIdFullData(metalId);
    }

    @Override
    public List<Metal> findAllByIds(Set<Long> metalIds) {
        return StreamSupport.stream(repository.findAllById(metalIds).spliterator(), false).toList();
    }

    @Override
    public Page<Metal> findAllByInventory(Long inventoryId, int page, int size) {
        Page<Long> materialIds = repository.findAllIdsByInventoryId(inventoryId, PageRequest.of(page, size));

        if(materialIds.isEmpty())
            return Page.empty();

        List<Metal> metals = repository.findAllByIdsAndInventoryFullData(materialIds.getContent(), inventoryId);

        return new PageImpl<>(metals, materialIds.getPageable(), materialIds.getTotalElements());
    }

    @Override
    public Page<Metal> findAllByCurrentUser(int page, int size) {
        Page<Long> materialIds = repository.findAllIdsByUserId(helperService.getCurrentUser().getId(), PageRequest.of(page, size));

        if(materialIds.isEmpty())
            return Page.empty();

        List<Metal> metals = repository.findAllByIdsAndUserFullData(materialIds.getContent(), helperService.getCurrentUser().getId());

        return new PageImpl<>(metals, materialIds.getPageable(), materialIds.getTotalElements());
    }

    @Override
    public Optional<MetalDto> findOneDto(Long materialId) {
        return repository.findByIdFullData(materialId).map(m -> (MetalDto)mapper.toDto(m));
    }

    @Override
    public Page<MetalDto> findAllByInventoryDto(Long inventoryId, int page, int size) {
        Page<Long> materialIds = repository.findAllIdsByInventoryId(inventoryId, PageRequest.of(page, size));

        if(materialIds.isEmpty())
            return Page.empty();

        List<Metal> metals = repository.findAllByIdsAndInventoryFullData(materialIds.getContent(), inventoryId);

        List<MetalDto> metalsDto = metals.stream().map(m -> (MetalDto)mapper.toDto(m)).toList();

        return new PageImpl<>(metalsDto, materialIds.getPageable(), materialIds.getTotalElements());
    }

    @Override
    public Page<MetalDto> findAllByCurrentUserDto(int page, int size) {
        Page<Long> materialIds = repository.findAllIdsByUserId(helperService.getCurrentUser().getId(), PageRequest.of(page, size));

        if(materialIds.isEmpty())
            return Page.empty();

        List<Metal> metals = repository.findAllByIdsAndUserFullData(materialIds.getContent(), helperService.getCurrentUser().getId());

        List<MetalDto> metalsDto = metals.stream().map(m -> (MetalDto)mapper.toDto(m)).toList();

        return new PageImpl<>(metalsDto, materialIds.getPageable(), materialIds.getTotalElements());
    }

}
