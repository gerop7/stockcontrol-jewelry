package com.gerop.stockcontrol.jewelry.sort;

import java.util.Set;

public class StoneSortValidator implements SortValidator{
    @Override
    public Set<String> allowedFields() {
        return Set.of("name", "stonestock");
    }
}
