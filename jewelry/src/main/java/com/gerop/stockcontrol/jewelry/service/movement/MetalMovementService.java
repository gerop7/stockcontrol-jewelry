package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.MetalMovement;
import com.gerop.stockcontrol.jewelry.repository.MetalMovementRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

@Service
public class MetalMovementService implements IMaterialMovementService<MetalMovement, Metal, Float> {
    @Autowired
    private MetalMovementRepository metalMovementRepository;
    @Autowired
    public UserServiceHelper userServiceHelper;

    public Optional<MetalMovement> inflow(Float quantity, Metal metal) {
        MetalMovement movement = new MetalMovement();
        movement.setDescription("Se agregaron "+quantity+"gr.\nAntes "+(metal.getWeight()-quantity)+".");
        movement.setMetal(metal);
        movement.setJewel(null);
        movement.setType(CompositionMovementType.RAW_MATERIAL_INFLOW);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setWeight(metal.getWeight());

        MetalMovement saved = metalMovementRepository.save(movement);
        return Optional.of(saved);
    }

    public Optional<MetalMovement> outflow(Float quantity, Metal metal) {
        MetalMovement movement = new MetalMovement();
        movement.setDescription("Se quitaron "+quantity+"gr.\nAntes "+(metal.getWeight()+quantity)+".");
        movement.setMetal(metal);
        movement.setJewel(null);
        movement.setType(CompositionMovementType.RAW_MATERIAL_OUTFLOW);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setWeight(metal.getWeight());

        MetalMovement saved = metalMovementRepository.save(movement);
        return Optional.of(saved);
    }

    public Optional<MetalMovement> jewelRegister(Metal metal, Jewel jewel) {
        MetalMovement movement = new MetalMovement();
        movement.setDescription("Se creo una nueva joya compuesta de "+metal.getName()+".");
        movement.setMetal(metal);
        movement.setJewel(jewel);
        movement.setType(CompositionMovementType.JEWEL_REGISTER);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setWeight(0f);

        MetalMovement saved = metalMovementRepository.save(movement);
        return Optional.of(saved);
    }

    public Optional<MetalMovement> jewelDeregister(Metal metal, Jewel jewel) {
        MetalMovement movement = new MetalMovement();
        movement.setDescription("Se elimino una joya compuesta de "+metal.getName()+".");
        movement.setMetal(metal);
        movement.setJewel(jewel);
        movement.setType(CompositionMovementType.JEWEL_DEREGISTER);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setWeight(0f);

        MetalMovement saved = metalMovementRepository.save(movement);
        return Optional.of(saved);
    }

}
