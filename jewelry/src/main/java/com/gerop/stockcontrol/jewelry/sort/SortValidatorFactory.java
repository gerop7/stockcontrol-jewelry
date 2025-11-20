package com.gerop.stockcontrol.jewelry.sort;

public class SortValidatorFactory {

    public static SortValidator forEntity(Class<?> clazz) {

        return switch (clazz.getSimpleName()) {
            case "Jewel" -> new JewelSortValidator();
            case "Metal", "Stone" -> new MaterialSortValidator();
            case "JewelMovement", "MetalMovement", "StoneMovement" -> new MovementSortValidator();
            case "Sale" -> new SaleSortValidator();
            case "PendingJewelRestock", "PendingMetalRestock", "PendingStoneRestock" -> new PendingRestockSortValidator();
            default -> () -> java.util.Set.of(); // no permite ningún campo
        };
    }
}
