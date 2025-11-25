package com.gerop.stockcontrol.jewelry.service.category;

import java.util.List;
import java.util.Set;

import com.gerop.stockcontrol.jewelry.exception.CategoryNotFoundException;
import com.gerop.stockcontrol.jewelry.mapper.CategoryMapper;
import com.gerop.stockcontrol.jewelry.model.dto.category.SubcategoryDto;
import com.gerop.stockcontrol.jewelry.model.entity.Category;

import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;
import com.gerop.stockcontrol.jewelry.service.permissions.SubcategoryPermissionsService;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;
import com.gerop.stockcontrol.jewelry.repository.SubcategoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;

import org.springframework.transaction.annotation.Transactional;

@Service
public class SubcategoryService extends AbstractCategoryService<Subcategory, SubcategoryDto> {
    private final CategoryService categoryService;

    public SubcategoryService(SubcategoryRepository repository, SubcategoryPermissionsService permissionsService, UserServiceHelper helper, IInventoryService inventoryService, CategoryMapper mapper, CategoryService categoryService) {
        super(repository, permissionsService, helper, inventoryService, mapper, dto -> new Subcategory(dto.name(), false, null));
        this.categoryService = categoryService;
    }

    @Override
    @Transactional
    protected void createInternal(Subcategory sub, SubcategoryDto dto, Long userId) {
        Category category = categoryService.findOneWithOwner(dto.principalCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Debes adjuntar una categoria padre existente para crear la subcategoría "+dto.name()+"."));
        if(category.getOwner().getId().equals(userId)){
            sub.setPrincipalCategory(category);
            dto.inventoryIds().forEach(
                    inv -> {
                        if(permissionsService.canCreate(userId, inv)){
                            inventoryService.findOne(inv)
                                    .ifPresent(i -> {
                                        sub.getInventories().add(i);
                                        category.getInventories().add(i);
                                    });
                        }
                    }
            );
        }
        categoryService.save(category);
    }

    @Override
    protected String className() {
        return "Subcategoría";
    }

    @Override
    protected void addToInventoryInternal(Subcategory sub, Inventory inventory) {
        sub.getInventories().add(inventory);
        Category category = sub.getPrincipalCategory();
        category.getInventories().add(inventory);
        categoryService.save(category);
    }

    @Override
    protected void removeFromInventoryInternal(Subcategory sub, Inventory inventory) {
        sub.getInventories().remove(inventory);
    }

    @Override
    protected Set<Inventory> getInventories(Subcategory sub) {
        return sub.getInventories();
    }

    @Transactional
    public void removeFromInventoryAllByPrincipalCategory(Category cat, Inventory inventory) {
        List<Subcategory> subcategories = findAllByPrincipalCategoryAndInventory(cat.getId(), inventory.getId());
        subcategories.stream().map(
                s -> {
                    s.getInventories().remove(inventory);
                    return repository.save(s);
                });
    }

    @Transactional(readOnly = true)
    public List<Subcategory> findAllByPrincipalCategoryAndInventory(Long principalCategoryId, Long inventoryId){
        return repository.findAllByPrincipalCategoryAndInventory(principalCategoryId,inventoryId);
    }

    public List<SubcategoryDto> findAllByPrincipalCategory(Long principalCategoryId){
        return repository.findAllByPrincipalCategory(principalCategoryId).stream().map(s -> (SubcategoryDto) mapper.toDto(s)).toList();
    }

    public List<SubcategoryDto> findAllByPrincipalCategoryInInventory(Long principalCategoryId, Long inventoryId){
        return findAllByPrincipalCategoryAndInventory(principalCategoryId,inventoryId).stream().map(s-> (SubcategoryDto) mapper.toDto(s)).toList();
    }
}
