package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Material;
import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.Movement;

public interface IMaterialMovementService<T extends Movement, M extends Material, Q extends Number>{
    T inflow(Q quantity, M mat, Inventory inventory);
    T outflow(Q quantity, M mat, Inventory inventory);
    T sale(M mat, Inventory inventory, Q quantity, Float total);
    T jewelRegister(M mat, Jewel jewel, Inventory inventory);
    T replacement(M mat, Q quantity, Inventory inventory);
    T marked_replacement(M mat, Q quantity, Inventory inventory);
    T saveMovement(M mat, Q quantity, Jewel jewel, String description, CompositionMovementType type, Inventory inventory);
    List<T> findAll();
    List<T> findAllByType(CompositionMovementType type);

}
