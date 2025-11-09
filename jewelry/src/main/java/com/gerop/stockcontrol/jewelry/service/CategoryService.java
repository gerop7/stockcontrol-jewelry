package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.CategoryNotAvaibleException;
import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.repository.CategoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.ICategoryService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.permissions.ICategoryPermissionsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository repository;
    private final IInventoryService inventoryService;
    private final ICategoryPermissionsService<Category> categoryPermissions;
    private final UserServiceHelper helper;

    @Override
    public Optional<Category> findOne(Long catId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public void addToInventories(Category cat, List<Inventory> inventories) {
        if(!cat.isGlobal()){
            Set<Long> existingIds = cat.getInventories().stream().map(Inventory::getId).collect(Collectors.toSet());

            inventories.forEach(
                // Los permisos WRITE en el inventario ya fueron validados previamente en JewelService.create()
                // Aquí solo se verifica propiedad de la categoría para evitar fugas cross-user.
                s -> {
                    if (!existingIds.contains(s.getId())) {
                    // Validar que el usuario actual sea dueño de la categoría
                    if (!categoryPermissions.isOwner(helper.getCurrentUser().getId(), cat))
                        throw new CategoryNotAvaibleException(
                            "No se puede asignar la categoría " + cat.getName() +
                            " al inventario " + s.getName() + ".");

                    cat.getInventories().add(s);
                }
            });
            save(cat);
        }
    }

    @Override
    public Optional<Category> findOneWithOwner(Long catId) {
        return repository.findByIdWithOwner(catId);
    }

    @Override
    public Category save(Category cat) {
        return repository.save(cat);
    }

}
