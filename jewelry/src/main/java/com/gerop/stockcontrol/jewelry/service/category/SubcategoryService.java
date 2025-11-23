package com.gerop.stockcontrol.jewelry.service.category;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.gerop.stockcontrol.jewelry.mapper.CategoryMapper;
import com.gerop.stockcontrol.jewelry.model.dto.category.SubcategoryDto;
import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.repository.BaseCategoryRepository;
import com.gerop.stockcontrol.jewelry.repository.CategoryRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;
import com.gerop.stockcontrol.jewelry.service.permissions.SubcategoryPermissionsService;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.exception.CategoryNotAvaibleException;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;
import com.gerop.stockcontrol.jewelry.repository.SubcategoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.permissions.ICategoryPermissionsService;

import lombok.RequiredArgsConstructor;

@Service
public class SubcategoryService extends AbstractCategoryService<Subcategory, SubcategoryDto> {
    private final CategoryRepository categoryRepository;

    public SubcategoryService(BaseCategoryRepository<Subcategory> repository, ICategoryPermissionsService<Subcategory> permissionsService, UserServiceHelper helper, IInventoryService inventoryService, Function<SubcategoryDto, Subcategory> CategoryFactory, CategoryMapper mapper, CategoryRepository categoryRepository) {
        super(repository, permissionsService, helper, inventoryService, CategoryFactory, mapper);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Subcategory> findOne(Long subcategoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public void addToInventories(Subcategory sub, List<Inventory> inventories) {
        if(!sub.isGlobal()){
            Long currentUserId = helper.getCurrentUser().getId();
            Set<Inventory> existingInv = sub.getInventories();
            inventories.forEach(
                inv -> {
                    if(!existingInv.contains(inv)){
                        if(!permissionsService.isOwner(currentUserId, sub.getId()))
                            throw new CategoryNotAvaibleException("No se puede asignar la subcategoria "+sub.getName()+", en el inventario "+inv.getName()+".");
                        
                        sub.getInventories().add(inv);

                        Category category = sub.getPrincipalCategory();
                        category.getInventories().add(inv);
                        categoryRepository.save(category);
                    }
                }
            );
            save(sub);
        }
    }

    @Override
    public Optional<Subcategory> findOneWithOwner(Long subId) {
        return repository.findByIdWithOwner(subId);
    }

    @Override
    public Subcategory save(Subcategory sub) {
        return repository.save(sub);
    }

}
