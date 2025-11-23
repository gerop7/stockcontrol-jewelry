package com.gerop.stockcontrol.jewelry.service.category;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.gerop.stockcontrol.jewelry.mapper.CategoryMapper;
import com.gerop.stockcontrol.jewelry.model.dto.category.CategoryDto;
import com.gerop.stockcontrol.jewelry.repository.BaseCategoryRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;
import com.gerop.stockcontrol.jewelry.service.permissions.CategoryPermissionsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.CategoryNotAvaibleException;
import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.repository.CategoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.permissions.ICategoryPermissionsService;

import lombok.RequiredArgsConstructor;

@Service
public class CategoryService extends AbstractCategoryService<Category, CategoryDto> {

    public CategoryService(BaseCategoryRepository<Category> repository, ICategoryPermissionsService<Category> permissionsService, UserServiceHelper helper, IInventoryService inventoryService, Function<CategoryDto, Category> CategoryFactory, CategoryMapper mapper) {
        super(repository, permissionsService, helper, inventoryService, CategoryFactory, mapper);
    }
}
    @Override
    @Transactional
    public void addToInventories(Category cat, List<Inventory> inventories) {
        if(!cat.isGlobal()){
            Set<Inventory> existingInv = cat.getInventories();
            Long currentUserId = helper.getCurrentUser().getId();
            inventories.forEach(
                s -> {
                    if (!existingInv.contains(s)) {
                        if (!permissionsService.isOwner(currentUserId, cat.getId()))
                            throw new CategoryNotAvaibleException("No se puede asignar la categoría " + cat.getName() +" al inventario " + s.getName() + ".");
                        cat.getInventories().add(s);
                    }
                });
            save(cat);
        }
    }
}
