package com.gerop.stockcontrol.jewelry.mapper;

import com.gerop.stockcontrol.jewelry.model.dto.InventoryStockDto;
import com.gerop.stockcontrol.jewelry.model.dto.InventoryWeightDto;
import com.gerop.stockcontrol.jewelry.model.dto.materials.MaterialDto;
import com.gerop.stockcontrol.jewelry.model.dto.materials.MetalDto;
import com.gerop.stockcontrol.jewelry.model.dto.materials.StoneDto;
import com.gerop.stockcontrol.jewelry.model.entity.Material;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MaterialMapper {
    public MaterialDto toDto(Material material) {


        Long userId = Optional.ofNullable(material.getUser()).map(User::getId).orElse(null);

        if (material instanceof Stone stone) {
            return new StoneDto(
                    stone.getId(),
                    stone.getName(),
                    userId,
                    stone.isGlobal(),
                    safeMap(stone.getStockByInventory(), s -> new InventoryStockDto(s.getId(), s.getStock())),
                    safeMap(stone.getPendingStoneRestock(), p -> new InventoryStockDto(p.getId(), p.getQuantity())),
                    stone.getUrlImage()
            );
        }

        if (material instanceof Metal metal) {
            return new MetalDto(
                    metal.getId(),
                    metal.getName(),
                    userId,
                    metal.isGlobal(),
                    safeMap(metal.getStockByInventory(), s -> new InventoryWeightDto(s.getId(), s.getStock())),
                    safeMap(metal.getPendingMetalRestock(), p -> new InventoryWeightDto(p.getId(), p.getWeight())),
                    metal.getUrlImage()
            );
        }

        throw new IllegalArgumentException("Tipo de material no soportado");
    }

    private <T, R> Set<R> safeMap(Collection<T> list, Function<T, R> mapper) {
        return Optional.ofNullable(list).orElse(Set.of()).stream().map(mapper).collect(Collectors.toSet());
    }
}
