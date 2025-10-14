package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Material;
import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.Movement;

public interface IMaterialMovementService<T extends Movement, M extends Material, Q extends Number>{
    T inflow(Q quantity, M mat);
    T outflow(Q quantity, M mat);
    T jewelRegister(M mat, Jewel jewel);
    T jewelDeregister(M mat, Jewel jewel);
    T replacement(M mat, Q quantity);
    T saveMovement(M mat, Q quantity, Jewel jewel, String description, CompositionMovementType type);
    List<T> findAll();
    List<T> findAllByType(CompositionMovementType type);
}
