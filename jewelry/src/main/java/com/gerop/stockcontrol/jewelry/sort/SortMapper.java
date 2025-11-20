package com.gerop.stockcontrol.jewelry.sort;

import org.springframework.data.domain.Sort;

public class SortMapper {
    public static Sort toSpringSort(SortDto dto) {
        if (dto == null || dto.sortBy() == null) {
            return Sort.unsorted();
        }

        return dto.direction() == SortDirection.ASC?Sort.by(Sort.Direction.ASC, dto.field())
                :Sort.by(Sort.Direction.DESC, dto.field());
    }
}
