package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.List;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Material;
import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.Movement;

public interface IMaterialMovementService<T extends Movement, M extends Material, Q extends Number>{
    Optional<T> inflow(Q quantity, M mat);
    Optional<T> outflow(Q quantity, M mat);
    Optional<T> jewelRegister(M mat, Jewel jewel);
    Optional<T> jewelDeregister(M mat, Jewel jewel);
    Optional<T> replacement(M mat, Q quantity);
    Optional<T> saveMovement(M mat, Q quantity, Jewel jewel, String description, CompositionMovementType type);
    List<T> findAll();
    List<T> findAllByType(CompositionMovementType type);
}
