package com.gerop.stockcontrol.jewelry.model.dto.category;

import java.util.Set;

public sealed interface ICategoryDto permits CategoryDto, SubcategoryDto {
    Long id();
    String name();
    Long userId();
    boolean global();
    Set<Long> inventoryIds();

}
