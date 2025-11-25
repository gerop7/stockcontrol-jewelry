package com.gerop.stockcontrol.jewelry.service.permissions;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubcategoryPermissionsService implements ICategoryPermissionsService<Subcategory>{



    @Override
    public boolean isOwner(Long ownerId, Long catId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isOwner'");
    }

    @Override
    public boolean isOwner(Long ownerId, Subcategory cat) {
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

    @Override
    public boolean canAddToInventory(Long categoryId, Long inventoryId, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canAddToInventory'");
    }

    @Override
    public boolean canUseToCreateWithoutInventoryCheck(Long inventoryId, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canUseToCreateWithoutInventoryCheck'");
    }

}
