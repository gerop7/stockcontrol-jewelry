package com.gerop.stockcontrol.jewelry.service.category;

import com.gerop.stockcontrol.jewelry.exception.CategoryNotAvaibleException;
import com.gerop.stockcontrol.jewelry.exception.CategoryNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.mapper.CategoryMapper;
import com.gerop.stockcontrol.jewelry.model.dto.category.ICategoryDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.AbstractCategory;
import com.gerop.stockcontrol.jewelry.model.entity.User;
import com.gerop.stockcontrol.jewelry.repository.BaseCategoryRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.permissions.ICategoryPermissionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class AbstractCategoryService<C extends AbstractCategory, CDto extends ICategoryDto> implements ICategoryService<C, CDto>{
    protected final BaseCategoryRepository<C> repository;
    protected final ICategoryPermissionsService<C> permissionsService;
    protected final UserServiceHelper helper;
    protected final IInventoryService inventoryService;
    protected final CategoryMapper mapper;
    protected final Function<CDto, C> CategoryFactory;

    protected abstract void createInternal(C cat, CDto dto, Long userId);
    protected abstract String className();
    protected abstract void addToInventoryInternal(C cat, Inventory inventory);
    protected abstract void removeFromInventoryInternal(C cat, Inventory inventory);
    protected abstract Set<Inventory> getInventories(C cat);

    @Override
    @Transactional
    public CDto create(CDto dto) {
        User user = helper.getCurrentUser();
        C category = CategoryFactory.apply(dto);
        category.setOwner(user);

        createInternal(category, dto, user.getId());

        save(category);
        return (CDto) mapper.toDto(category);
    }

    @Override
    @Transactional
    public C save(C cat) {
        return repository.save(cat);
    }

    protected void validateFields(C cat, List<Inventory> inventories){
        if(inventories==null || inventories.isEmpty() || cat==null)
            throw new RequiredFieldException("No se puede agregar la "+getClass()+" a ningún inventario.");
    }

    @Override
    @Transactional
    public void addToInventories(C cat, List<Inventory> inventories) {
        validateFields(cat, inventories);

        if(!cat.isGlobal()){
            Long currentUserId = helper.getCurrentUser().getId();
            Set<Inventory> existingInv = getInventories(cat);
            inventories.forEach(
                    inventory -> {
                        if(!existingInv.contains(inventory)){
                            if(permissionsService.isOwner(currentUserId, cat.getId())){
                                addToInventoryInternal(cat,inventory);
                            }
                        }
                    }
            );
            save(cat);
        }
    }

    @Override
    @Transactional
    public void addToInventories(Long catId, List<Long> inventoriesIds) {
        C cat = repository.findByIdWithOwner(catId).orElseThrow(() -> new CategoryNotFoundException(catId, className()));
        inventoriesIds = inventoryService.validateWritePermissions(inventoriesIds);
        List<Inventory> inventories = inventoryService.findAll(inventoriesIds);

        addToInventories(cat, inventories);
    }

    @Override
    @Transactional
    public void addToInventory(Long catId, Inventory inventory){
        C cat = repository.findByIdWithOwner(catId).orElseThrow(() -> new CategoryNotFoundException(catId, className()));
        addToInventories(cat, List.of(inventory));
    }

    @Override
    @Transactional
    public void removeFromInventories(C cat, List<Inventory> inventories) {
        validateFields(cat, inventories);
        User currentUser = helper.getCurrentUser();
        Set<Inventory> existingInv = getInventories(cat);
        if(!cat.isGlobal()){
            inventories.forEach(
                    inventory -> {
                        if(existingInv.contains(inventory) && permissionsService.canDeleteFromInventory(cat.getId(), inventory.getId(), currentUser.getId())){
                            removeFromInventoryInternal(cat, inventory);
                        }
                    });
            save(cat);
        }
    }

    @Override
    @Transactional
    public void removeFromInventories(Long catId, List<Long> inventoriesIds) {
        C cat = repository.findByIdWithOwner(catId).orElseThrow(() -> new CategoryNotFoundException(catId, className()));
        inventoriesIds = inventoryService.validateWritePermissions(inventoriesIds);
        List<Inventory> inventories = inventoryService.findAll(inventoriesIds);

        removeFromInventories(cat, inventories);
    }

    @Override
    @Transactional
    public void removeFromInventory(Long catId, Inventory inventory){
        C cat = repository.findByIdWithOwner(catId).orElseThrow(() -> new CategoryNotFoundException(catId, className()));
        removeFromInventories(cat, List.of(inventory));
    }

    @Override
    public List<CDto> findAllByUser(Long userId) {
        return (List<CDto>) repository.findAllByUser(userId).stream().map(mapper::toDto).toList();
    }

    @Override
    public List<CDto> findAllByInventory(Long inventoryId) {
        return (List<CDto>) repository.findAllByInventory(inventoryId).stream().map(mapper::toDto).toList();
    }

    @Override
    public List<CDto> findAllByUserNotInInventory(Long inventoryId) {
        return (List<CDto>) repository.findAllByUserNotInInventory(helper.getCurrentUser().getId(),inventoryId).stream().map(mapper::toDto).toList();
    }

    @Override
    public List<CDto> findAllToCreateInInventory(Long inventoryId){
        List<C> catByUser = repository.findAllByUser(helper.getCurrentUser().getId());
        List<C> catByInv = repository.findAllByInventory(inventoryId);

        Set<C> result = new HashSet<>();
        result.addAll(catByInv);
        result.addAll(catByUser);

        return (List<CDto>) result.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<CDto> findAllContainsName(List<CDto> categories, String string){
        return categories.stream().filter(c -> c.name() != null && c.name().toLowerCase().contains(string.toLowerCase())).toList();
    }

    @Override
    public Optional<C> findOneWithOwner(Long catId) {
        return repository.findByIdWithOwner(catId);
    }

    @Override
    public Optional<C> findOne(Long catId) {
        return Optional.of(
                repository.findById(catId)
                        .orElseThrow(() -> new CategoryNotFoundException(catId, className()))
        );
    }
}
