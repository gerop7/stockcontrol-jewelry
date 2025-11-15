package com.gerop.stockcontrol.jewelry.model.dto.materials;

public sealed interface MaterialDto permits MetalDto, StoneDto {
    Long id();
    String name();
    Long userId();
    boolean global();
    String url();
}
