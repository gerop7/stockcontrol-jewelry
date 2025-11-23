package com.gerop.stockcontrol.jewelry.sort;

import org.springframework.data.domain.Sort;

public class SortMapper {

    public static Sort toSpringSort(SortDto dto) {
        if (dto == null || dto.sortBy() == null) {
            return Sort.unsorted();
        }

        String mappedField = mapField(dto.sortBy());

        return dto.direction() == SortDirection.ASC
                ? Sort.by(Sort.Direction.ASC, mappedField)
                : Sort.by(Sort.Direction.DESC, mappedField);
    }

    private static String mapField(String sortBy) {
        if (sortBy == null) return null;

        return switch (sortBy.toLowerCase()) {
            case "jewelstock","metalstock", "stonestock" -> "stockByInventory.stock";
            default -> sortBy.toLowerCase();
        };
    }
}