package com.gerop.stockcontrol.jewelry.sort;

public enum SortDirection {
    ASC,
    DESC;

    public static SortDirection fromString(String value) {
        if (value == null) return ASC;
        return value.equalsIgnoreCase("desc") ? DESC : ASC;
    }
}
