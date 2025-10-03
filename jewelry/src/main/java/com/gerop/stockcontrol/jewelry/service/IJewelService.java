package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.dto.JewelDto;
import com.gerop.stockcontrol.jewelry.model.dto.MetalWeightDto;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;

public interface IJewelService {
    Optional<Jewel> create(JewelDto jewelDto);

    Optional<Jewel> updateName(Long id, String name);

    Optional<Jewel> updateDescription(Long id,String description);

    Optional<Jewel> addStock(Long id, Long quantity);
    Optional<Jewel> removeStock(Long id, Long quantity);

    Optional<Jewel> sale(Long quantity, Long id, boolean jewelToRestock, List<MetalWeightDto> metalToRestock);

    Optional<Jewel> findById(Long id);

    List<Jewel> findAll();
}
