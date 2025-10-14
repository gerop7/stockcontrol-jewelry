package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.MetalMovement;
import com.gerop.stockcontrol.jewelry.repository.MetalMovementRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

@Service
@Transactional
public class MetalMovementService implements IMaterialMovementService<MetalMovement, Metal, Float> {
    private final MetalMovementRepository metalMovementRepository;
    private final UserServiceHelper userServiceHelper;

    public MetalMovementService(MetalMovementRepository metalMovementRepository, UserServiceHelper userServiceHelper){
        this.metalMovementRepository= metalMovementRepository;
        this.userServiceHelper = userServiceHelper;
    }

    @Override
    public MetalMovement inflow(Float quantity, Metal metal) {
        String description =("Se agregaron "+quantity+"gr.\nAntes "+(metal.getWeight()-quantity)+".");
        return saveMovement(metal, quantity,null,description,CompositionMovementType.RAW_MATERIAL_INFLOW);
    }

    @Override
    public MetalMovement outflow(Float quantity, Metal metal) {
        String description =("Se quitaron "+quantity+"gr.\nAntes "+(metal.getWeight()+quantity)+".");

        return saveMovement(metal, quantity,null,description,CompositionMovementType.RAW_MATERIAL_OUTFLOW);
    }

    @Override
    public MetalMovement jewelRegister(Metal metal, Jewel jewel) {
        String description =("Se creo una joya compuesta de "+metal.getName()+".");
        return saveMovement(metal, 0f, jewel, description, CompositionMovementType.JEWEL_REGISTER);
    }

    @Override
    public MetalMovement jewelDeregister(Metal metal, Jewel jewel) {
        String description = ("Se elimino una joya compuesta de "+metal.getName()+".");
        
        return saveMovement(metal, 0f, jewel, description, CompositionMovementType.JEWEL_DEREGISTER);
    }

    @Override
    public MetalMovement replacement(Metal metal, Float quantity) {
        String description = ("Se repuso "+quantity+" gramos.");
        return saveMovement(metal, quantity,null,description,CompositionMovementType.REPLACEMENT);
    }

    @Override
    public MetalMovement saveMovement(Metal metal, Float quantity, Jewel jewel, String description, CompositionMovementType type) {
        MetalMovement movement = new MetalMovement();
        movement.setDescription(description);
        movement.setMetal(metal);
        movement.setJewel(jewel);
        movement.setType(type);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setWeight(quantity);

        return metalMovementRepository.save(movement);
    }

    @Override
    @Transactional(readOnly=true)
    public List<MetalMovement> findAll() {
        return metalMovementRepository.findAllByUserOrderByTimestampDesc(userServiceHelper.getCurrentUser());
    }

    @Override
    @Transactional(readOnly=true)
    public List<MetalMovement> findAllByType(CompositionMovementType type) {
        return metalMovementRepository.findAllByUserAndTypeOrderByTimestampDesc(userServiceHelper.getCurrentUser(),type);
    }

}
