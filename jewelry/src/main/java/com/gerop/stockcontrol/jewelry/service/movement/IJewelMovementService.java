package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.enums.JewelMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.JewelMovement;

public interface IJewelMovementService {
    JewelMovement create(Jewel jewel, Inventory inventory);
    JewelMovement modify(String modifyDescription, Jewel jewel, Inventory inventory);
    JewelMovement addStock(Jewel jewel, Long quantity, Inventory inventory, String description);
    JewelMovement sale(Jewel jewel, Long quantity, Float total, Inventory inventory);
    JewelMovement replacement(Jewel jewel, Long quantity, Inventory inventory);
    JewelMovement marked_replacement(Jewel jewel, Long quantity, Inventory inventory);
    List<JewelMovement> findAll();
    List<JewelMovement> findAllByType(JewelMovementType type);
}
