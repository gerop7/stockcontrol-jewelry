package com.gerop.stockcontrol.jewelry.sort;

public record SortDto(
        String sortBy,
        String sortDir
) {
    public String field() {
        return sortBy;
    }

    public SortDirection direction() {
        return SortDirection.fromString(sortDir);
    }
}
