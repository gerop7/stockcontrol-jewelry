package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.movement.JewelMovement;

public interface IJewelMovementService {
    Optional<JewelMovement> create(Jewel jewel);

    Optional<JewelMovement> modify(String modifyDescription, Jewel jewel);

    Optional<JewelMovement> delete(Jewel jewel);

    Optional<JewelMovement> addStock(Jewel jewel, Long quantity);
    Optional<JewelMovement> sale(Jewel jewel, Long quantity, Float total);

}
