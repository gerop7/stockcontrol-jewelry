package com.gerop.stockcontrol.jewelry.service;

import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Movement;

public interface IMovementService {
    Optional<Movement> create(Jewel jewel);

    Optional<Movement> modify(String modifyDescription, Jewel jewel);

    Optional<Movement> delete(Jewel jewel);

    Optional<Movement> addStock(Jewel jewel, Long quantity);

    Optional<Movement> removeStock(Jewel jewel, Long quantity);
}
