package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.entity.Composition;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.movement.CompositionMovement;

public interface ICompositionMovementService {
    Optional<CompositionMovement> inflow(Float quantity, Composition composition);
    Optional<CompositionMovement> outflow(Float quantity, Composition composition);
    Optional<CompositionMovement> jewelRegister(Composition composition, Jewel jewel);
    Optional<CompositionMovement> jewelDeregister(Composition composition, Jewel jewel);
}
