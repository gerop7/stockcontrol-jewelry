package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.enums.JewelMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.JewelMovement;

public interface IJewelMovementService {
    JewelMovement create(Jewel jewel);
    JewelMovement modify(String modifyDescription, Jewel jewel);
    JewelMovement addStock(Jewel jewel, Long quantity);
    JewelMovement sale(Jewel jewel, Long quantity, Float total);
    JewelMovement replacement(Jewel jewel, Long quantity);
    List<JewelMovement> findAll();
    List<JewelMovement> findAllByType(JewelMovementType type);
}
