package com.gerop.stockcontrol.jewelry.sort;

import java.util.Set;

public class SortValidatorFactory {

    public static SortValidator forEntity(Class<?> clazz) {

        return switch (clazz.getSimpleName()) {
            case "Jewel" -> new JewelSortValidator();
            case "Metal" -> new MetalSortValidator();
            case "Stone" -> new StoneSortValidator();
            case "JewelMovement", "MetalMovement", "StoneMovement" -> new MovementSortValidator();
            case "Sale" -> new SaleSortValidator();
            case "PendingJewelRestock", "PendingMetalRestock", "PendingStoneRestock" -> new PendingRestockSortValidator();
            default -> Set::of;
        };
    }
}
