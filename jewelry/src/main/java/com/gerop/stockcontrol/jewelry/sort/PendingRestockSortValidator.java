package com.gerop.stockcontrol.jewelry.sort;

import java.util.Set;

public class PendingRestockSortValidator implements SortValidator{
    @Override
    public Set<String> allowedFields() {
        return Set.of("createdAt","quantity","weight");
    }
}
