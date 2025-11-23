package com.gerop.stockcontrol.jewelry.sort;

import java.util.Set;

public interface SortValidator {
    Set<String> allowedFields();

    default boolean isValid(String sortBy) {
        if (sortBy == null)
            return true;
        return allowedFields().contains(sortBy.toLowerCase());
    }
}
