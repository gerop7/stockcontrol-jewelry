package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.enums.JewelMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.JewelMovement;
import com.gerop.stockcontrol.jewelry.repository.JewelMovementRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

@Service
@Transactional
public class JewelMovementService implements IJewelMovementService {
    private final JewelMovementRepository movementRepository;
    private final MetalMovementService metalMovementService;
    private final StoneMovementService stoneMovementService;
    private final UserServiceHelper userServiceHelper;

    

    public JewelMovementService(JewelMovementRepository movementRepository, MetalMovementService metalMovementService,
            StoneMovementService stoneMovementService, UserServiceHelper userServiceHelper) {
        this.movementRepository = movementRepository;
        this.metalMovementService = metalMovementService;
        this.stoneMovementService = stoneMovementService;
        this.userServiceHelper = userServiceHelper;
    }

    @Override
    public JewelMovement create(Jewel jewel, Inventory inventory) {
        String description=("Articulo: "+jewel.getSku() + " Creado exitosamente en el inventario "+inventory.getName()+"!");
        List<Metal> metals = jewel.getMetal();
        metals.forEach(m -> metalMovementService.jewelRegister(m, jewel, inventory));

        if(!jewel.getStone().isEmpty()){
            List<Stone> stones = jewel.getStone();
            stones.forEach(s -> stoneMovementService.jewelRegister(s, jewel, inventory));
        }

        return saveMovement(jewel, 0L, description, JewelMovementType.CREATE, inventory);
    }

    @Override
    public JewelMovement modify(String modifyDescription, Jewel jewel, Inventory inventory) {  
        return saveMovement(jewel, 0L, modifyDescription, JewelMovementType.MODIFY, inventory);
    }

    @Override
    public JewelMovement addStock(Jewel jewel, Long quantity, Inventory inventory) {
        String description = ("Se añadio "+quantity+" unidades de "+ jewel.getSku()+" al inventario "+inventory.getName()+".");
        
        return saveMovement(jewel, quantity, description, JewelMovementType.STOCK_ADD, inventory);
    }

    @Override
    public JewelMovement sale(Jewel jewel, Long quantity, Float total, Inventory inventory) {
        StringBuilder description = new StringBuilder("Venta de ")
            .append(jewel.getSku()).append(".\n Total= $")
            .append(total.toString()).append(". En el inventario ").append(inventory.getName());
        if(quantity>1)
            description.append(".\nSe vendieron ").append(quantity).append(" Unidades.");
        else
            description.append(".\nSe vendio 1 Unidad.");
        
        return saveMovement(jewel, quantity, description.toString(), JewelMovementType.SALE, inventory);
    }

    @Override
    public JewelMovement replacement(Jewel jewel, Long quantity, Inventory inventory) {
        StringBuilder description = new StringBuilder("Se repuso ").append(jewel.getSku()).append(" en el Inventario ").append(inventory.getName());
        if(quantity>1)
            description.append(".\nUn total de ").append(quantity).append(" Unidades.");
        else
            description.append(".\nUna unica unidad.");

        return saveMovement(jewel, quantity, description.toString(), JewelMovementType.REPLACEMENT, inventory);
    }

    
    public JewelMovement saveMovement(Jewel jewel, Long quantity, String description, JewelMovementType type, Inventory inventory) {
        
        JewelMovement movement = new JewelMovement();
        movement.setDescription(description);
        movement.setJewel(jewel);
        movement.setType(type);
        movement.setQuantity(quantity);
        movement.setUser(userServiceHelper.getCurrentUser());
        movement.setInventory(inventory);
        
        return movementRepository.save(movement);
    }

    @Override
    @Transactional(readOnly=true)
    public List<JewelMovement> findAll() {
        return movementRepository.findAllByUserIdOrderByTimestampDesc(userServiceHelper.getCurrentUser().getId());
    }

    @Override
    @Transactional(readOnly=true)
    public List<JewelMovement> findAllByType(JewelMovementType type) {
        return movementRepository.findAllByUserIdAndTypeOrderByTimestampDesc(userServiceHelper.getCurrentUser().getId(),type);
    }
}
