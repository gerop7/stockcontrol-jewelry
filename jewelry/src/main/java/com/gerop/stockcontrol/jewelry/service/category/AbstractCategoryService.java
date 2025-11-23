package com.gerop.stockcontrol.jewelry.service.category;

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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractCategoryService<C extends AbstractCategory, CDto extends ICategoryDto> implements ICategoryService<C, CDto>{
    protected final BaseCategoryRepository<C> repository;
    protected final ICategoryPermissionsService<C> permissionsService;
    protected final UserServiceHelper helper;
    protected final IInventoryService inventoryService;
    protected final Function<CDto, C> CategoryFactory;
    protected final CategoryMapper mapper;

    protected abstract void createInternal(C cat, CDto dto);
    protected abstract void addToInventoryInternal(C cat, List<Inventory> inventories);
    protected abstract String className();


    @Override
    @Transactional
    public CDto create(CDto dto) {
        User user = helper.getCurrentUser();
        C category = CategoryFactory.apply(dto);
        category.setOwner(user);
        save(category);

        createInternal(category, dto);

        return (CDto) mapper.toDto(save(category));
    }

    @Override
    public C save(C cat) {
        repository.save(cat);
    }

    @Override
    public void addToInventories(C cat, List<Inventory> inventories) {
        if(inventories==null || inventories.isEmpty() || cat==null)
            throw new RequiredFieldException("No se puede agregar la "+getClass()+" a ningún inventario.");

        if(!cat.isGlobal()){
            addToInventoryInternal(cat, inventories);
            save(cat);
        }
    }

    @Override
    public void addToInventories(Long catId, List<Long> inventoriesIds) {
        C cat = repository.findByIdWithOwner(catId).orElseThrow(() -> new CategoryNotFoundException(catId, className()));
        List<Inventory> inventories = inventoryService.findAll(inventoriesIds);

        addToInventories(cat, inventories);
    }

    @Override
    public void addToInventory(Long catId, Inventory inventory){
        C cat = repository.findByIdWithOwner(catId).orElseThrow(() -> new CategoryNotFoundException(catId, className()));
        addToInventories(cat, List.of(inventory));
    }

    @Override
    public void removeFromInventories(C cat, List<Inventory> inventories) {

    }

    @Override
    public void removeFromInventories(Long catId, List<Long> inventoriesIds) {

    }

    @Override
    public List<CDto> findAllByUser(Long userId) {
        return null;
    }

    @Override
    public Set<CDto> findAllByInventory(Long userId, Long inventoryId) {
        return Set.of();
    }

    @Override
    public List<CDto> findAllByUserNotInInventory(Long userId, Long inventoryId) {
        return List.of();
    }

    @Override
    public CDto findById(Long id) {
        return null;
    }

    @Override
    public Optional<C> findOneWithOwner(Long catId) {
        return Optional.empty();
    }

    @Override
    public Optional<C> findOne(Long catId) {
        return Optional.empty();
    }
}
