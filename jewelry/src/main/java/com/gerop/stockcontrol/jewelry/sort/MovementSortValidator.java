package com.gerop.stockcontrol.jewelry.sort;

import java.util.Set;

public class MovementSortValidator implements SortValidator{
    @Override
    public Set<String> allowedFields() {
        return Set.of("timestamp");
    }
}
