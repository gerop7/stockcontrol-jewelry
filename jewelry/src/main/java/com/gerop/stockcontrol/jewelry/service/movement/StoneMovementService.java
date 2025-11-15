package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.StoneMovement;
import com.gerop.stockcontrol.jewelry.repository.StoneMovementRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

@Service
@Transactional
public class StoneMovementService implements IMaterialMovementService<StoneMovement, Stone, Long>{

    private final StoneMovementRepository stoneMovementRepository;
    private final UserServiceHelper userServiceHelper;

    public StoneMovementService(StoneMovementRepository movementRepository, UserServiceHelper userServiceHelper) {
        this.stoneMovementRepository = movementRepository;
        this.userServiceHelper = userServiceHelper;
    }

    @Override
    public StoneMovement inflow(Long quantity, Stone mat, Inventory inventory) {
        StringBuilder description = new StringBuilder("Se agregaron ");
        description.append(quantity).append(" unidades de ").append(mat.getName()).append(" al inventario ")
        .append(inventory.getName()).append(".");
        return saveMovement(mat, quantity, null, description.toString(), CompositionMovementType.RAW_MATERIAL_INFLOW,inventory);
    }

    @Override
    public StoneMovement outflow(Long quantity, Stone mat, Inventory inventory) {
        StringBuilder description = new StringBuilder("Se quitaron ");
        description.append(quantity).append(" unidades de ").append(mat.getName()).append(" al inventario ")
        .append(inventory.getName()).append(".");
        return saveMovement(mat, quantity, null, description.toString(), CompositionMovementType.RAW_MATERIAL_OUTFLOW, inventory);
    }

    @Override
    public StoneMovement sale(Stone mat, Inventory inventory, Long quantity, Float total) {
        return null;
    }

    @Override
    public StoneMovement jewelRegister(Stone mat, Jewel jewel, Inventory inventory) {
        String description =("Se creo "+jewel.getSku()+" compuesta de "+mat.getName()+"en el inventario"+mat.getName()+".");
        return saveMovement(mat, 0L, jewel, description, CompositionMovementType.JEWEL_REGISTER, inventory);
    }

    @Override
    public StoneMovement replacement(Stone mat, Long quantity, Inventory inventory) {
        StringBuilder description= new StringBuilder("Se repuso ");
        if(quantity>1)
            description.append(quantity).append(" unidades ");
        else
            description.append(quantity).append(" unidad ");
        description.append("de ").append(mat.getName()).append(" en el inventario ").append(inventory.getName()).append(".");
        return saveMovement(mat, quantity, null, description.toString(), CompositionMovementType.REPLACEMENT, inventory);
    }
    
    @Override
    public StoneMovement marked_replacement(Stone stone, Long quantity, Inventory inventory) {
        StringBuilder description= new StringBuilder("Se deben reponer ");
        if(quantity>1)
            description.append(quantity).append(" unidades ");
        else
            description.append(quantity).append(" unidad ");
        description.append("de ").append(stone.getName()).append(" en el inventario ").append(inventory.getName()).append(".");
        return saveMovement(stone, quantity, null, description.toString(), CompositionMovementType.REPLACEMENT, inventory);
    }

    @Override
    public StoneMovement saveMovement(Stone stone, Long quantity, Jewel jewel, String description, CompositionMovementType type, Inventory inventory) {
        StoneMovement movement = new StoneMovement();

        movement.setDescription(description);
        movement.setJewel(jewel);
        movement.setQuantity(quantity);
        movement.setStone(stone);
        movement.setType(type);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setInventory(inventory);
        
        return stoneMovementRepository.save(movement);
    }

    @Override
    @Transactional(readOnly=true)
    public List<StoneMovement> findAll() {
        return stoneMovementRepository.findAllByUserIdOrderByTimestampDesc(userServiceHelper.getCurrentUser().getId());
    }

    @Override
    @Transactional(readOnly=true)
    public List<StoneMovement> findAllByType(CompositionMovementType type) {
        return stoneMovementRepository.findAllByUserIdAndTypeOrderByTimestampDesc(userServiceHelper.getCurrentUser().getId(),type);
    }

}
