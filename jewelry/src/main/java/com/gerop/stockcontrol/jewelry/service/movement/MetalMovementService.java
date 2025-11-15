package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
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
    public MetalMovement inflow(Float quantity, Metal metal, Inventory inventory) {
        String description ="Se agregaron "+quantity+"gr. De "+metal.getName()+" al inventario "+inventory.getName()+".";
        return saveMovement(metal, quantity,null,description,CompositionMovementType.RAW_MATERIAL_INFLOW,inventory);
    }

    @Override
    public MetalMovement outflow(Float quantity, Metal metal, Inventory inventory) {
        String description ="Se quitaron "+quantity+"gr. De "+metal.getName()+" al inventario "+inventory.getName()+".";

        return saveMovement(metal, quantity,null,description,CompositionMovementType.RAW_MATERIAL_OUTFLOW,inventory);
    }

    @Override
    public MetalMovement sale(Metal mat, Inventory inventory, Float quantity, Float total) {
        return null;
    }

    @Override
    public MetalMovement jewelRegister(Metal metal, Jewel jewel, Inventory inventory) {
        String description =("Se creo "+jewel.getSku()+" compuesta de "+metal.getName()+"en el inventario"+metal.getName()+".");
        return saveMovement(metal, 0f, jewel, description, CompositionMovementType.JEWEL_REGISTER,inventory);
    }

    @Override
    public MetalMovement replacement(Metal metal, Float quantity, Inventory inventory) {
        String description = ("Se repuso "+quantity+" gr. De "+metal.getName()+" en el inventario "+inventory.getName());
        return saveMovement(metal, quantity,null,description,CompositionMovementType.REPLACEMENT,inventory);
    }
    
    @Override
    public MetalMovement marked_replacement(Metal metal, Float quantity, Inventory inventory) {
        String description = ("Se deben reponer "+quantity+" gr. De "+metal.getName()+" en el inventario "+inventory.getName()+".");
        return saveMovement(metal, quantity, null, description, CompositionMovementType.MARKED_FOR_RESTOCK, inventory);
    }

    @Override
    public MetalMovement saveMovement(Metal metal, Float quantity, Jewel jewel, String description, CompositionMovementType type, Inventory inventory) {
        MetalMovement movement = new MetalMovement();
        movement.setDescription(description);
        movement.setMetal(metal);
        movement.setJewel(jewel);
        movement.setType(type);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setWeight(quantity);
        movement.setInventory(inventory);

        return metalMovementRepository.save(movement);
    }

    @Override
    @Transactional(readOnly=true)
    public List<MetalMovement> findAll() {
        return metalMovementRepository.findAllByUserIdOrderByTimestampDesc(userServiceHelper.getCurrentUser().getId());
    }

    @Override
    @Transactional(readOnly=true)
    public List<MetalMovement> findAllByType(CompositionMovementType type) {
        return metalMovementRepository.findAllByUserIdAndTypeOrderByTimestampDesc(userServiceHelper.getCurrentUser().getId(),type);
    }


}
