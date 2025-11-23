package com.gerop.stockcontrol.jewelry.model.dto.materials;

import java.util.Set;

public sealed interface MaterialDto permits MetalDto, StoneDto {
    Long id();
    String name();
    Long userId();
    boolean global();
    String url();
    Set<?> stockByInventory();
    Set<?> pendingRestock();
}
