package com.gerop.stockcontrol.jewelry.model.dto.category;

import java.util.Set;

public record CategoryDto(
        Long id,
        String name,
        Long userId,
        boolean global,
        Set<Long> inventoryIds
) implements ICategoryDto {
}
