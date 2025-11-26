package com.gerop.stockcontrol.jewelry.service.permissions;

import com.gerop.stockcontrol.jewelry.exception.CategoryNotFoundException;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryPermissionsService implements ICategoryPermissionsService<Category> {
    private final IInventoryPermissionsService inventoryPermissions;
    private final CategoryRepository repository;

    @Override
    public boolean isOwner(Long ownerId, Long catId) {
        return repository.existsByIdAndOwnerId(catId, ownerId);
    }

    @Override
    public boolean isOwner(Long ownerId, Category cat) {
        return cat.getOwner().getId().equals(ownerId);
    }

    @Override
    public boolean canCreate(Long userId, Long inventoryId) {
        return inventoryPermissions.canWrite(inventoryId,userId);
    }

    @Override
    //No se puede eliminar si es global, y puede eliminar el dueño de la categoria o del inventario.
    public boolean canDeleteFromInventory(Long categoryId, Long inventoryId, Long userId) {
        Category category = repository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId, "Categoria"));

        if(category.isGlobal()) return false;

        return isOwner(userId, category) || inventoryPermissions.isOwner(inventoryId,userId);
    }

}
