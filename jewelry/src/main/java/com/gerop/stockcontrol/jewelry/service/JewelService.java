package com.gerop.stockcontrol.jewelry.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.CategoryNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.exception.inventory.InventoryAccessDeniedException;
import com.gerop.stockcontrol.jewelry.exception.inventory.InventoryNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.jewel.JewelNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.jewel.JewelPermissionDeniedException;
import com.gerop.stockcontrol.jewelry.mapper.JewelMapper;
import com.gerop.stockcontrol.jewelry.model.dto.JewelDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateJewelDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.SaleJewel;
import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;
import com.gerop.stockcontrol.jewelry.model.entity.User;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingJewelRestock;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;
import com.gerop.stockcontrol.jewelry.repository.JewelRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.ICategoryService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IJewelService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IStockByInventoryService;
import com.gerop.stockcontrol.jewelry.service.interfaces.ISubcategoryService;
import com.gerop.stockcontrol.jewelry.service.movement.IJewelMovementService;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.IPendingRestockService;
import com.gerop.stockcontrol.jewelry.service.permissions.JewelPermissionsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JewelService implements IJewelService{

    private final JewelPermissionsService jewelPermissionsService;
    private final MetalService metalService;
    private final StoneService stoneService;
    private final JewelRepository jewelRepository;
    private final IJewelMovementService movementService;
    private final UserServiceHelper userServiceHelper;
    private final IPendingRestockService<PendingJewelRestock, Long, Jewel> pendingRestockService;
    private final ICategoryService categoryService;
    private final ISubcategoryService subcategoryService;
    private final JewelMapper mapper;
    private final IStockByInventoryService<JewelryStockByInventory ,Jewel, Long> stockService;
    private final IInventoryService invService;

    @Override
    @Transactional
    public JewelDto create(JewelDto jewelDto) {
        User currentUser=userServiceHelper.getCurrentUser();
        Jewel jewel = new Jewel(jewelDto.name(), jewelDto.description(), 
            jewelDto.sku(),jewelDto.imageUrl(), jewelDto.weight(), 
            jewelDto.size(), currentUser);

        save(jewel);
        
        Long subId=jewelDto.subcategoryId();
        List<Inventory> inventories = new ArrayList<>();

        jewelDto.stockByInventory().forEach(
            inv ->
            {
                Long inventoryId = inv.inventoryId();
                Inventory inventory = invService.findOne(inventoryId)
                    .orElseThrow(()-> new InventoryNotFoundException("El inventario con ID: "+inventoryId+" no existe!"));
                jewelPermissionsService.canCreate(inventoryId, currentUser.getId(), jewelDto.metalIds(), jewelDto.stoneIds());

                if(inv.stock()!=null && inv.stock()>0 )
                    jewel.getStockByInventory().add(stockService.create(jewel, inventory, inv.stock()));
                else
                    throw new InvalidQuantityException(inv.stock());
                jewel.getPendingRestock().add(pendingRestockService.create(jewel, inventory));
                jewel.getInventories().add(inventory);  

                inventories.add(inventory);
            }
        );

        if(subId!=null){
            Subcategory sub = subcategoryService.findOneWithOwner(subId).orElseThrow(()-> new CategoryNotFoundException(subId,"Subcategoría"));
            subcategoryService.addToInventories(sub, inventories);
            jewel.setSubcategory(sub);
            jewel.setCategory(sub.getPrincipalCategory());
        }else{
            Long catId=jewelDto.categoryId();
            if(catId!=null){
                Category cat = categoryService.findOneWithOwner(catId).orElseThrow(()-> new CategoryNotFoundException(catId,"Categoría"));
                categoryService.addToInventories(cat, inventories);
                jewel.setCategory(cat);
            }else{
                throw new RequiredFieldException("Es necesaria una categoria o subcategoria para crear la joya "+jewelDto.sku()+".");
            }
        }

        jewel.getMetal().addAll(metalService.findAllByIds(jewelDto.metalIds()));
        jewel.getStone().addAll(stoneService.findAllByIds(jewelDto.stoneIds()));

        inventories.forEach(
            i->{
                movementService.create(jewel, i);
            }
        );
        
        save(jewel);
        return mapper.toDto(jewel);
    }

    @Override
    public Boolean delete(Long id) {
        Jewel jewel = jewelRepository.findById(id).orElseThrow(()-> new JewelNotFoundException(id));
        jewel.setActive(false);

        save(jewel);

        return true;
    }

    @Override
    @Transactional
    public Jewel save(Jewel jewel) {
        return jewelRepository.save(jewel);
    }

    @Override
    @Transactional
    public JewelDto update(Long id, UpdateJewelDataDto updateData) {
        if(!jewelPermissionsService.canEditInfo(id, userServiceHelper.getCurrentUser().getId()))
            throw new JewelPermissionDeniedException("No tienes permisos para editar la joya con ID: "+id);

        Jewel jewel = jewelRepository.findByIdWithInventories(id)
            .orElseThrow(() -> new JewelNotFoundException(id));
        
        String name = updateData.name();
        String description = updateData.description();
        String url = updateData.imageUrl();
        String sku = updateData.sku();
        Float weight = updateData.weight();
        Float size = updateData.size();

        StringBuilder desc = new StringBuilder("Cambios realizados en la joya ").append(jewel.getSku()).append(": \n");

        if(name!=null){
            desc.append(updateDescription(jewel.getName(), name, "el nombre"));
            jewel.setName(name);
        }
            

        if(description!=null){
            desc.append(updateDescription(jewel.getDescription(), description, "la descripcion"));
            jewel.setDescription(description);
        }

        if(url!=null){
            desc.append("Se modifico la Imagen.");
            jewel.setImageUrl(url);
        }

        if(sku!=null){
            desc.append(updateDescription(jewel.getSku(),sku,"el código"));
            jewel.setSku(sku);
        }
    
        if(weight!=null){
            if(weight<0)
                throw new InvalidQuantityException(weight);
            desc.append(updateDescription(jewel.getWeight().toString(),weight.toString(),"el peso"));
            jewel.setWeight(weight);
        }
        
        if(size!=null){
            if(size<0)
                throw new InvalidQuantityException(size);   
            desc.append(updateDescription(jewel.getSize().toString(),size.toString(),"el largo"));
            jewel.setSize(size);
        }

        jewel.getInventories().forEach(
            inventory -> {
                movementService.modify(desc.toString(), jewel, inventory);
            }
        );
        
        save(jewel);
        return mapper.toDto(jewel);
    }

    public String updateDescription(String original, String update, String field){
        return "Se modifico "+field+". Antes: "+original+". Ahora: "+update+".\n";
    }

    @Override
    @Transactional
    public JewelDto addStock(Long id, Long inventoryId, Long quantity, String description) {
        Inventory inventory = invService.findOne(inventoryId)
            .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
        Jewel jewel = jewelRepository.findByIdWithStockByInventory(id)
            .orElseThrow(() -> new JewelNotFoundException(id));
        if(!jewelPermissionsService.canModifyStock(id, inventoryId, userServiceHelper.getCurrentUser().getId()))
            throw new JewelPermissionDeniedException("No tienes permiso para agregar stock a la joya "+jewel.getSku()+", en el inventario "+inventory.getName()+".");
        
        stockService.addStock(jewel, inventory, quantity);
        
        pendingRestockService.removeFromRestock(jewel, inventory, quantity);
        movementService.addStock(jewel, quantity, inventory);
        return mapper.toDto(save(jewel));
    }

    @Override
    @Transactional
    public SaleJewel sale(Long jewelId, Long quantity, Long quantityToRestock, Float total, Inventory inventory) {
        Jewel jewel = jewelRepository.findByIdWithMaterials(jewelId).orElseThrow(()-> new JewelNotFoundException("La joya con "+jewelId+" no existe!"));
        if(!jewelPermissionsService.canModifyStock(jewelId, inventory.getId(), userServiceHelper.getCurrentUser().getId())){
            throw new InventoryAccessDeniedException("No estas autorizado a modificar el stock de la joya "+jewel.getSku()+" en el inventario "+inventory.getName());
        }

        stockService.removeStock(jewel, inventory, quantity);

        handleAddPendingToRestock(jewel, inventory, quantityToRestock);

        movementService.sale(jewel, quantity, total, inventory);

        return new SaleJewel(jewel, quantity, total);
    }

    @Override
    public Optional<JewelDto> findByIdDto(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<JewelDto> findAllDto() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Jewel> findById(Long jewelId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Jewel> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    @Transactional(readOnly=true)
    public boolean haveStones(Long jewelId){
        return jewelRepository.existsByIdAndHasStones(jewelId);
    }
    
    @Override
    public void addPendingToRestock(Long jewelId, Long inventoryId, Long quantity) {
        throw new UnsupportedOperationException("Unimplemented method 'addPendingToRestock'");
    }
    
    @Transactional
    public void handleAddPendingToRestock(Jewel jewel, Inventory inventory, Long quantity) {
        if(quantity!= null && quantity>0){
            pendingRestockService.addToRestock(jewel, inventory, quantity);
            movementService.marked_replacement(jewel, quantity, inventory);
        } else
            throw new InvalidQuantityException("La cantidad a reponer de la joya "+jewel.getSku()+" es invalida!");
    }

    @Override
    public void removePendingToRestock(Jewel jewel, Inventory inventory, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean existsByIdAndHasOneMetal(Jewel jewel, Long metalId){
        boolean hasMetal = jewel
            .getMetal().stream()
            .anyMatch(m -> m.getId().equals(metalId));
        
        return hasMetal;
    }

    @Override
    @Transactional(readOnly=true)
    public boolean existsByIdAndHasOneStone(Jewel jewel, Long stoneId){
        boolean hasStone = jewel
            .getStone().stream()
            .anyMatch(s -> s.getId().equals(stoneId));

        return hasStone;
    }

}
