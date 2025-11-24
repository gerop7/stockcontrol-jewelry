package com.gerop.stockcontrol.jewelry.service.category;

import com.gerop.stockcontrol.jewelry.mapper.CategoryMapper;
import com.gerop.stockcontrol.jewelry.model.dto.category.CategoryDto;
import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.User;
import com.gerop.stockcontrol.jewelry.repository.CategoryRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.permissions.CategoryPermissionsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public class CategoryService extends AbstractCategoryService<Category, CategoryDto> {
    private final SubcategoryService subcategoryService;

    public CategoryService(CategoryRepository repository, CategoryPermissionsService permissionsService, UserServiceHelper helper, IInventoryService inventoryService, CategoryMapper mapper, SubcategoryService subcategoryService) {
        super(repository, permissionsService, helper, inventoryService, mapper, dto -> new Category(dto.name(), false, null));
        this.subcategoryService = subcategoryService;
    }

    @Override
    protected void createInternal(Category cat, CategoryDto dto, Long userId) {
        dto.inventoryIds().forEach(
                inv -> {
                    if(permissionsService.canCreate(userId, inv)){
                        inventoryService.findOne(inv)
                                .ifPresent(i -> cat.getInventories().add(i));
                    }
                }
        );
    }

    @Override
    protected String className() {
        return "Categoría";
    }

    @Override
    protected void addToInventoryInternal(Category cat, Inventory inventory) {
        cat.getInventories().add(inventory);
    }

    @Override
    @Transactional
    protected void removeFromInventoryInternal(Category cat, Inventory inventory) {
        cat.getInventories().remove(inventory);
        subcategoryService.removeFromInventoryAllByPrincipalCategory(cat, inventory);
    }

    @Override
    protected Set<Inventory> getInventories(Category cat) {
        return cat.getInventories();
    }

}
