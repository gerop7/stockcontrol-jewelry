package com.gerop.stockcontrol.jewelry.service.permissions;

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
    public boolean canAddToInventory(Long categoryId, Long inventoryId, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canAddToInventory'");
    }

    @Override
    public boolean canUseToCreateWithoutInventoryCheck(Long inventoryId, Long userId) {
        return false;
    }

    @Override
    public boolean isOwner(Long ownerId, Long jewelId) {
        return false;
    }

    @Override
    public boolean isOwner(Long ownerId, Category cat) {
        return cat.getOwner().getId().equals(ownerId);
    }

    @Override
    public boolean canCreate(Long userId, Long inventoryId) {
        return false;
    }

    @Override
    //No se puede eliminar si es global, y puede eliminar el dueño de la categoria o del inventario.
    public boolean canDeleteFromInventory(Long categoryId, Long inventoryId, Long userId) {
        return false;
    }

}
