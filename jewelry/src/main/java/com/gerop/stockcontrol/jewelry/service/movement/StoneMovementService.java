package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.StoneMovement;
import com.gerop.stockcontrol.jewelry.repository.StoneMovementRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

@Service
public class StoneMovementService implements IMaterialMovementService<StoneMovement, Stone, Long>{

    private final StoneMovementRepository stoneMovementRepository;
    private final UserServiceHelper userServiceHelper;

    public StoneMovementService(StoneMovementRepository movementRepository, UserServiceHelper userServiceHelper) {
        this.stoneMovementRepository = movementRepository;
        this.userServiceHelper = userServiceHelper;
    }

    @Override
    public Optional<StoneMovement> inflow(Long quantity, Stone mat) {
        StringBuilder description = new StringBuilder("Se agregaron ");
        description.append(quantity).append(" unidades de ").append(mat.getName()).append(".");
        return saveMovement(mat, quantity, null, description.toString(), CompositionMovementType.RAW_MATERIAL_INFLOW);
    }

    @Override
    public Optional<StoneMovement> outflow(Long quantity, Stone mat) {
        StringBuilder description = new StringBuilder("Se quitaron ");
        description.append(quantity).append(" unidades de ").append(mat.getName()).append(".");
        return saveMovement(mat, quantity, null, description.toString(), CompositionMovementType.RAW_MATERIAL_OUTFLOW);
    }

    @Override
    public Optional<StoneMovement> jewelRegister(Stone mat, Jewel jewel) {
        String description = "Se creo una joya compuesta de "+mat.getName()+".";
        return saveMovement(mat, 0L, jewel, description, CompositionMovementType.JEWEL_REGISTER);
    }

    @Override
    public Optional<StoneMovement> jewelDeregister(Stone mat, Jewel jewel) {
        String description = "Se elimino una joya compuesta de "+mat.getName()+".";
        return saveMovement(mat, 0L, jewel, description, CompositionMovementType.JEWEL_DEREGISTER);
    }

    @Override
    public Optional<StoneMovement> replacement(Stone mat, Long quantity) {
        StringBuilder description= new StringBuilder("Se repuso ");
        if(quantity>1)
            description.append(quantity).append(" unidades ");
        else
            description.append(quantity).append(" unidad ");
        return saveMovement(mat, quantity, null, description.toString(), CompositionMovementType.REPLACEMENT);
    }

    @Override
    public Optional<StoneMovement> saveMovement(Stone stone, Long quantity, Jewel jewel, String description, CompositionMovementType type) {
        StoneMovement movement = new StoneMovement();

        movement.setDescription(description);
        movement.setJewel(jewel);
        movement.setQuantity(quantity);
        movement.setStone(stone);
        movement.setType(type);
        movement.setUser(userServiceHelper.getCurrentUser());
        
        return Optional.of(stoneMovementRepository.save(movement));
    }

    @Override
    public List<StoneMovement> findAll() {
        return stoneMovementRepository.findAllByOrderByTimestampDesc();
    }

    @Override
    public List<StoneMovement> findAllByType(CompositionMovementType type) {
        return stoneMovementRepository.findAllByTypeOrderByTimestampDesc(type);
    }
}
