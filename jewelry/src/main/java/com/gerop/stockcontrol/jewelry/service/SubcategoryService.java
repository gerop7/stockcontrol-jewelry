package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.exception.CategoryNotAvaibleException;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;
import com.gerop.stockcontrol.jewelry.repository.SubcategoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.interfaces.ISubcategoryService;
import com.gerop.stockcontrol.jewelry.service.permissions.ICategoryPermissionsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubcategoryService implements ISubcategoryService {
    private final SubcategoryRepository repository;
    private final IInventoryService inventoryService;
    private final ICategoryPermissionsService<Subcategory> subcategoryPermissions;
    private final UserServiceHelper helper;

    @Override
    public Optional<Subcategory> findOne(Long subcategoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public void addToInventories(Subcategory sub, List<Inventory> inventories) {
        if(!sub.isGlobal()){
            Set<Long> existingIds = sub.getInventories().stream().map(Inventory::getId).collect(Collectors.toSet());
            inventories.forEach(
                inv -> {
                    if(!existingIds.contains(inv.getId())){
                        if(!subcategoryPermissions.isOwner(helper.getCurrentUser().getId(), sub))
                            throw new CategoryNotAvaibleException("No se puede asignar la subcategoria "+sub.getName()+", en el inventario "+inv.getName()+".");
                        
                        sub.getInventories().add(inv);
                        sub.getPrincipalCategory().getInventories().add(inv);
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
