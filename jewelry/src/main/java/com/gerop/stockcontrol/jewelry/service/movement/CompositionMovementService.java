package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Composition;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.CompositionMovement;
import com.gerop.stockcontrol.jewelry.repository.CompositionMovementRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

@Service
public class CompositionMovementService implements ICompositionMovementService {
    @Autowired
    private CompositionMovementRepository compositionMovementRepository;
    @Autowired
    public UserServiceHelper userServiceHelper;

    @Override
    public Optional<CompositionMovement> inflow(Float quantity, Composition composition) {
        CompositionMovement movement = new CompositionMovement();
        movement.setDescription("Se agregaron "+quantity+"gr.\nAntes "+(composition.getWeight()-quantity)+".");
        movement.setComposition(composition);
        movement.setJewel(null);
        movement.setType(CompositionMovementType.RAW_MATERIAL_INFLOW);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setWeight(composition.getWeight());

        CompositionMovement saved = compositionMovementRepository.save(movement);
        return Optional.of(saved);
    }

    @Override
    public Optional<CompositionMovement> outflow(Float quantity, Composition composition) {
        CompositionMovement movement = new CompositionMovement();
        movement.setDescription("Se quitaron "+quantity+"gr.\nAntes "+(composition.getWeight()+quantity)+".");
        movement.setComposition(composition);
        movement.setJewel(null);
        movement.setType(CompositionMovementType.RAW_MATERIAL_OUTFLOW);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setWeight(movement.getWeight());

        CompositionMovement saved = compositionMovementRepository.save(movement);
        return Optional.of(saved);
    }

    @Override
    public Optional<CompositionMovement> jewelRegister(Composition composition, Jewel jewel) {
        CompositionMovement movement = new CompositionMovement();
        movement.setDescription("Se creo una nueva joya compuesta de "+composition.getName()+".");
        movement.setComposition(composition);
        movement.setJewel(jewel);
        movement.setType(CompositionMovementType.JEWEL_REGISTER);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setWeight(0f);

        CompositionMovement saved = compositionMovementRepository.save(movement);
        return Optional.of(saved);
    }

    @Override
    public Optional<CompositionMovement> jewelDeregister(Composition composition, Jewel jewel) {
        CompositionMovement movement = new CompositionMovement();
        movement.setDescription("Se elimino una joya compuesta de "+composition.getName()+".");
        movement.setComposition(composition);
        movement.setJewel(jewel);
        movement.setType(CompositionMovementType.JEWEL_DEREGISTER);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setWeight(0f);

        CompositionMovement saved = compositionMovementRepository.save(movement);
        return Optional.of(saved);
    }

}
