package com.gerop.stockcontrol.jewelry.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.dto.JewelFilterDto;
import com.gerop.stockcontrol.jewelry.repository.spec.JewelSpecifications;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.PendingJewelRestockService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.CategoryNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
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
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;
import com.gerop.stockcontrol.jewelry.repository.JewelRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.ICategoryService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IJewelService;
import com.gerop.stockcontrol.jewelry.service.interfaces.ISubcategoryService;
import com.gerop.stockcontrol.jewelry.service.movement.IJewelMovementService;
import com.gerop.stockcontrol.jewelry.service.permissions.JewelPermissionsService;
import com.gerop.stockcontrol.jewelry.service.stockperinventory.JewelryStockByInventoryService;

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
    private final PendingJewelRestockService pendingRestockService;
    private final ICategoryService categoryService;
    private final ISubcategoryService subcategoryService;
    private final JewelMapper mapper;
    private final JewelryStockByInventoryService stockService;
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

                if(inv.stock()!=null && inv.stock()>=0 )
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
            i-> movementService.create(jewel, i)
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
    public void update(Long id, UpdateJewelDataDto updateData) {
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
            inventory -> movementService.modify(desc.toString(), jewel, inventory)
        );
        
        save(jewel);
    }

    public String updateDescription(String original, String update, String field){
        return "Se modifico "+field+". Antes: "+original+". Ahora: "+update+".\n";
    }

    @Override
    @Transactional
    public void addStock(Long id, Long inventoryId, Long quantity, String description) {
        Inventory inventory = invService.findOne(inventoryId)
            .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
        Jewel jewel = jewelRepository.findByIdWithStockByInventory(id)
            .orElseThrow(() -> new JewelNotFoundException(id));
        if(!jewelPermissionsService.canModifyStock(id, inventoryId, userServiceHelper.getCurrentUser().getId()))
            throw new JewelPermissionDeniedException("No tienes permiso para agregar stock a la joya "+jewel.getSku()+", en el inventario "+inventory.getName()+".");
        
        stockService.addStock(jewel, inventory, quantity);
        
        pendingRestockService.removeFromRestock(jewel, inventory, quantity);
        movementService.addStock(jewel, quantity, inventory, description);

        save(jewel);
    }

    @Override
    @Transactional
    public SaleJewel sale(Long jewelId, Long quantity, Long quantityToRestock, Float total, Inventory inventory) {
        Jewel jewel = jewelRepository.findByIdWithMaterials(jewelId).orElseThrow(()-> new JewelNotFoundException("La joya con "+jewelId+" no existe!"));
        if(!jewelPermissionsService.canModifyStock(jewelId, inventory.getId(), userServiceHelper.getCurrentUser().getId())){
            throw new InventoryAccessDeniedException("No estas autorizado a modificar el stock de la joya "+jewel.getSku()+" en el inventario "+inventory.getName());
        }

        stockService.removeStock(jewel, inventory, quantity);

        pendingRestockService.addToRestock(jewel, inventory, quantityToRestock);
        movementService.sale(jewel, quantity, total, inventory);

        return new SaleJewel(jewel, quantity, total);
    }
    
    @Override
    public void addPendingToRestock(Long jewelId, Long inventoryId, Long quantity) {
        Jewel jewel = jewelRepository.findById(jewelId)
            .orElseThrow(()-> new JewelNotFoundException("La joya con "+jewelId+" no existe!"));
        Inventory inventory = invService.findOne(inventoryId)
            .orElseThrow(() -> new InventoryNotFoundException("El inventario con ID: "+inventoryId+" no existe!"));
        if(!jewelPermissionsService.canModifyStock(jewelId, inventory.getId(), userServiceHelper.getCurrentUser().getId())){
            throw new InventoryAccessDeniedException("No estas autorizado a modificar el stock de la joya "+jewel.getSku()+" en el inventario "+inventory.getName());
        }

        pendingRestockService.addToRestock(jewel, inventory, quantity);
    }
    
    @Override
    public void removePendingToRestock(Long jewelId, Long inventoryId, Long quantity) {
        Jewel jewel = jewelRepository.findById(jewelId)
            .orElseThrow(()-> new JewelNotFoundException("La joya con "+jewelId+" no existe!"));
        Inventory inventory = invService.findOne(inventoryId)
            .orElseThrow(() -> new InventoryNotFoundException("El inventario con ID: "+inventoryId+" no existe!"));
        if(!jewelPermissionsService.canModifyStock(jewelId, inventory.getId(), userServiceHelper.getCurrentUser().getId())){
            throw new InventoryAccessDeniedException("No estas autorizado a modificar el stock de la joya "+jewel.getSku()+" en el inventario "+inventory.getName()+".");
        }

        pendingRestockService.removeFromRestock(jewel, inventory, quantity);
    }
    
    @Override
    @Transactional
    public void addToInventory(Long id, Long inventoryId, Long quantity) {
        Jewel jewel = jewelRepository.findByIdWithStockByInventory(id)
            .orElseThrow(()-> new JewelNotFoundException("La joya con "+id+" no existe!"));
        Inventory inventory = invService.findOne(inventoryId)
            .orElseThrow(() -> new InventoryNotFoundException("El inventario con ID: "+inventoryId+" no existe!"));
        if(!jewelPermissionsService.canAddToInventory(id, userServiceHelper.getCurrentUser().getId(), inventoryId))
            throw new InventoryAccessDeniedException("No estas autorizado para agregar la joya "+jewel.getSku()+" en el inventario "+inventory.getName()+".");
        
        jewel.getStockByInventory().add(stockService.create(jewel, inventory, quantity));
        jewel.getInventories().add(inventory);

        save(jewel);
    }

    @Override
    @Transactional
    public void removeFromInventory(Long id, Long inventoryId) {
        Jewel jewel = jewelRepository.findByIdWithStockByInventory(id)
            .orElseThrow(()-> new JewelNotFoundException("La joya con "+id+" no existe!"));
        Inventory inventory = invService.findOne(inventoryId)
            .orElseThrow(() -> new InventoryNotFoundException("El inventario con ID: "+inventoryId+" no existe!"));
        if(!jewelPermissionsService.canRemoveFromInventory(id, userServiceHelper.getCurrentUser().getId(), inventoryId))
            throw new InventoryAccessDeniedException("No estas autorizado para eliminar la joya "+jewel.getSku()+" en el inventario "+inventory.getName()+".");
        
        JewelryStockByInventory stock = jewel.getStockByInventory().stream().filter(s -> s.getInventory().equals(inventory)).findFirst()
            .orElseThrow(() -> new StockNotFoundException("No existe registro de stock para la joya "+ jewel.getSku() + " en el inventario " + inventory.getName() + "."));

        jewel.getInventories().remove(inventory);
        stockService.remove(stock);

        pendingRestockService.remove(jewel,inventory);

        save(jewel);
    }

    @Override
    @Transactional(readOnly=true)
    public Optional<JewelDto> findByIdDto(Long id) {
        return jewelRepository.findByIdFullData(id).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JewelDto> findAllByCurrentUserDto(int page, int size) {
        Long userId = userServiceHelper.getCurrentUser().getId();

        Page<Long> jewelIdsPage = jewelRepository.findAllIdsByUserId(userId, PageRequest.of(page, size));
        if (jewelIdsPage.isEmpty()) return Page.empty();

        List<Jewel> jewels = jewelRepository.findAllByIdsWithFullData(jewelIdsPage.getContent());
        List<JewelDto> jewelDtos = jewels.stream().map(mapper::toDto).toList();
        return new PageImpl<>(jewelDtos, jewelIdsPage.getPageable(), jewelIdsPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JewelDto> findAllByInventoryDto(Long inventoryId, int page, int size) {
        Page<Long> jewelIdsPage = jewelRepository.findAllIdsByInventoryId(inventoryId, PageRequest.of(page, size));

        if (jewelIdsPage.isEmpty()) return Page.empty();

        List<Jewel> jewels = jewelRepository.findAllByIdsWithFullData(jewelIdsPage.getContent());

        jewels.forEach(jewel -> filterByInventory(jewel, inventoryId));

        List<JewelDto> jewelDtos = jewels.stream().map(mapper::toDto).toList();

        return new PageImpl<>(jewelDtos, jewelIdsPage.getPageable(), jewelIdsPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly=true)
    public Optional<Jewel> findById(Long jewelId) {
        return jewelRepository.findByIdFullData(jewelId);
    }

    @Override
    @Transactional(readOnly=true)
    public Page<Jewel> findAllByCurrentUser(int page, int size) {
        Long userId = userServiceHelper.getCurrentUser().getId();

        Page<Long> jewelIdsPage = jewelRepository.findAllIdsByUserId(userId, PageRequest.of(page, size));
        if (jewelIdsPage.isEmpty()) return Page.empty();

        List<Jewel> jewels = jewelRepository.findAllByIdsWithFullDataByUser(jewelIdsPage.getContent(), userId);

        return new PageImpl<>(jewels, jewelIdsPage.getPageable(), jewelIdsPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly=true)
    public Page<Jewel> findAllByInventory(Long inventoryId, int page, int size) {
        Page<Long> jewelIdsPage = jewelRepository.findAllIdsByInventoryId(inventoryId, PageRequest.of(page, size));
        if (jewelIdsPage.isEmpty()) return Page.empty();

        List<Jewel> jewels = jewelRepository.findAllByIdsWithFullData(jewelIdsPage.getContent());

        jewels.forEach(jewel -> filterByInventory(jewel, inventoryId));

        return new PageImpl<>(jewels, jewelIdsPage.getPageable(), jewelIdsPage.getTotalElements());
    }

    private void filterByInventory(Jewel j, Long inventoryId) {
        j.setPendingRestock(j.getPendingRestock().stream()
                .filter(p -> p.getInventory().getId().equals(inventoryId))
                .toList());

        j.setStockByInventory(j.getStockByInventory().stream()
                .filter(s -> s.getInventory().getId().equals(inventoryId))
                .toList());
    }

    @Override
    @Transactional(readOnly=true)
    public Optional<JewelDto> findByIdAndInventoryIdDto(Long id, Long inventoryId) {
        return findByIdAndInventoryId(id, inventoryId).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly=true)
    public Optional<Jewel> findByIdAndInventoryId(Long id, Long inventoryId) {
        return jewelRepository.findByIdFullData(id).map(jewel -> {
            filterByInventory(jewel, inventoryId);
            return jewel;
        });
    }

    @Override
    public Page<JewelDto> filterMyJewels(JewelFilterDto f, int page, int size) {
        Long userId = userServiceHelper.getCurrentUser().getId();

        Specification<Jewel> spec = Specification.allOf(
                JewelSpecifications.belongsToUser(userId),
                JewelSpecifications.nameContains(f.name()),
                JewelSpecifications.skuEquals(f.sku()),
                JewelSpecifications.categoryIs(f.categoryId()),
                JewelSpecifications.subcategoryIs(f.subcategoryId()),
                JewelSpecifications.hasMetal(f.metalIds()),
                JewelSpecifications.hasStone(f.stoneIds()),
                JewelSpecifications.weightBetween(f.minWeight(), f.maxWeight()),
                JewelSpecifications.sizeBetween(f.minSize(), f.maxSize()),
                JewelSpecifications.activeIs(f.active())
        );

        Page<Jewel> idsPage = jewelRepository.findAll(spec, PageRequest.of(page, size));
        if (idsPage.isEmpty()) return Page.empty();

        List<Long> ids = idsPage.getContent().stream().map(Jewel::getId).toList();

        List<JewelDto> dtos = jewelRepository.findAllByIdsWithFullDataByUser(ids, userId).stream().map(mapper::toDto).toList();

        return new PageImpl<>(dtos, idsPage.getPageable(), idsPage.getTotalElements());
    }

    @Override
    public Page<JewelDto> filterJewels(JewelFilterDto f, Long inventoryId, int page, int size) {
        if(inventoryId==null)
            throw new RequiredFieldException("Debes indicar un Inventario!");

        Long userId = userServiceHelper.getCurrentUser().getId();

        Specification<Jewel> spec = Specification.allOf(
                JewelSpecifications.belongsToUser(userId),
                JewelSpecifications.inInventory(inventoryId),
                JewelSpecifications.nameContains(f.name()),
                JewelSpecifications.skuEquals(f.sku()),
                JewelSpecifications.categoryIs(f.categoryId()),
                JewelSpecifications.subcategoryIs(f.subcategoryId()),
                JewelSpecifications.hasMetal(f.metalIds()),
                JewelSpecifications.hasStone(f.stoneIds()),
                JewelSpecifications.weightBetween(f.minWeight(), f.maxWeight()),
                JewelSpecifications.sizeBetween(f.minSize(), f.maxSize()),
                JewelSpecifications.activeIs(f.active())
        );

        Page<Jewel> idsPage = jewelRepository.findAll(spec, PageRequest.of(page, size));

        if (idsPage.isEmpty()) return Page.empty();

        List<Long> ids = idsPage.getContent().stream()
                .map(Jewel::getId)
                .toList();

        List<JewelDto> dtos = jewelRepository.findAllByIdsWithFullData(ids).parallelStream().peek(j -> filterByInventory(j, inventoryId))
                .filter(j -> filterByStock(j, f.minStock(), f.maxStock())).filter(j -> filterByPending(j, f.hasPendingRestock()))
                .map(mapper::toDto).toList();

        return new PageImpl<>(dtos, idsPage.getPageable(), idsPage.getTotalElements());
    }

    private boolean filterByStock(Jewel j, Float min, Float max) {
        if (min == null && max == null) return true;

        long stock = j.getStockByInventory().isEmpty()?0:j.getStockByInventory().get(0).getQuantity();

        if (min != null && stock < min) return false;
        if (max != null && stock > max) return false;

        return true;
    }

    private boolean filterByPending(Jewel j, Boolean hasPending) {
        if (hasPending == null) return true;

        boolean actual = !j.getPendingRestock().isEmpty();

        return hasPending.equals(actual);
    }


    @Override
    @Transactional(readOnly=true)
    public boolean haveStones(Long jewelId){
        return jewelRepository.existsByIdAndHasStones(jewelId);
    }
    

    @Override
    public boolean existsByIdAndHasOneMetal(Jewel jewel, Long metalId){
        return jewel
            .getMetal().stream()
            .anyMatch(m -> m.getId().equals(metalId));
    }

    @Override
    public boolean existsByIdAndHasOneStone(Jewel jewel, Long stoneId){
        return jewel
            .getStone().stream()
            .anyMatch(s -> s.getId().equals(stoneId));


    }

}
