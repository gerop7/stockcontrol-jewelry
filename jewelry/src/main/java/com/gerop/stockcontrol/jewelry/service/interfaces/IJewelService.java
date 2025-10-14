package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.dto.JewelDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateJewelDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;

public interface IJewelService {
    Jewel create(JewelDto jewelDto);
    Boolean delete(Long id);
    Jewel save(Jewel jewel);
    
    JewelDto update(Long id, UpdateJewelDataDto updateData);

    JewelDto addStock(Long id, Long quantity);
    JewelDto sale(Long id,Long quantity, Long quantityToRestock);

    Optional<Jewel> findById(Long id);
    List<Jewel> findAll();
}
