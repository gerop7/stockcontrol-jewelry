package com.gerop.stockcontrol.jewelry.model.dto.category;

import java.util.Set;

public record SubcategoryDto(
        Long id,
        String name,
        Long userId,
        boolean global,
        Set<Long> inventoryIds,
        Long principalCategoryId,
        String principalCategoryName
) implements ICategoryDto {}
