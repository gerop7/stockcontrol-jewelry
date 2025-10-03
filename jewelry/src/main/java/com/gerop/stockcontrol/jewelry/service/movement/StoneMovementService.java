package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.movement.StoneMovement;
import com.gerop.stockcontrol.jewelry.repository.MetalMovementRepository;
import com.gerop.stockcontrol.jewelry.repository.StoneMovementRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

public class StoneMovementService implements IMaterialMovementService<StoneMovement, Stone, Long>{

    @Autowired
    private StoneMovementRepository stoneMovementRepository;
    @Autowired
    public UserServiceHelper userServiceHelper;

    @Override
    public Optional<StoneMovement> inflow(Float quantity, Stone mat) {
        
    }

    @Override
    public Optional<StoneMovement> outflow(Float quantity, Stone mat) {
    }

    @Override
    public Optional<StoneMovement> jewelRegister(Stone mat, Jewel jewel) {
    }

    @Override
    public Optional<StoneMovement> jewelDeregister(Stone mat, Jewel jewel) {
    }

}
