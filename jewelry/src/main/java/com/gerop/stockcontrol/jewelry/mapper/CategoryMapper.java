package com.gerop.stockcontrol.jewelry.mapper;

import com.gerop.stockcontrol.jewelry.exception.CategoryNotAvaibleException;
import com.gerop.stockcontrol.jewelry.model.dto.category.CategoryDto;
import com.gerop.stockcontrol.jewelry.model.dto.category.ICategoryDto;
import com.gerop.stockcontrol.jewelry.model.dto.category.SubcategoryDto;
import com.gerop.stockcontrol.jewelry.model.entity.*;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CategoryMapper {
    public ICategoryDto toDto(AbstractCategory category){
        Long userId = Optional.ofNullable(category.getOwner()).map(User::getId).orElse(null);
        if(category instanceof Category cat){
            return new CategoryDto(
                    cat.getId(),
                    cat.getName(),
                    userId,
                    cat.isGlobal(),
                    safeMap(cat.getInventories(), Inventory::getId)
            );
        } else if (category instanceof Subcategory sub) {
            return new SubcategoryDto(
                    sub.getId(),
                    sub.getName(),
                    userId,
                    sub.isGlobal(),
                    safeMap(sub.getInventories(), Inventory::getId),
                    Optional.ofNullable(sub.getPrincipalCategory()).map(AbstractCategory::getId).orElse(null),
                    Optional.ofNullable(sub.getPrincipalCategory()).map(AbstractCategory::getName).orElse(null)
            );
        }

        throw new CategoryNotAvaibleException("Tipo de categoría no valida");
    }

    private <T, R> Set<R> safeMap(Collection<T> list, Function<T, R> mapper) {
        return Optional.ofNullable(list).orElse(Set.of()).stream().map(mapper).collect(Collectors.toSet());
    }
}
