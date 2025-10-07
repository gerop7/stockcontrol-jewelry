package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.enums.JewelMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.JewelMovement;
import com.gerop.stockcontrol.jewelry.repository.JewelMovementRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

@Service
public class JewelMovementService implements IJewelMovementService {
    private final JewelMovementRepository movementRepository;
    private final UserServiceHelper userServiceHelper;

    

    public JewelMovementService(JewelMovementRepository movementRepository, UserServiceHelper userServiceHelper) {
        this.movementRepository = movementRepository;
        this.userServiceHelper = userServiceHelper;
    }

    @Override
    public Optional<JewelMovement> create(Jewel jewel) {
        String description=("Articulo: "+jewel.getName() + " Creado exitosamente!");

        return saveMovement(jewel, 0L, description, JewelMovementType.CREATE);
    }

    @Override
    public Optional<JewelMovement> modify(String modifyDescription, Jewel jewel) {  
        return saveMovement(jewel, 0L, modifyDescription, JewelMovementType.MODIFY);
    }

    @Override
    public Optional<JewelMovement> delete(Jewel jewel) {
        String description =("Se elimino el articulo: "+jewel.getName());
        
        return saveMovement(jewel, 0L, description, JewelMovementType.DELETE);
    }

    @Override
    public Optional<JewelMovement> addStock(Jewel jewel, Long quantity) {
        String description = ("Se añadio "+quantity+" unidades de "+ jewel.getName()+ 
            ". Antes: "+(jewel.getStock()-quantity)+" unidades");
        
        return saveMovement(jewel, quantity, description, JewelMovementType.STOCK_ADD);
    }

    @Override
    public Optional<JewelMovement> sale(Jewel jewel, Long quantity, Float total) {
        StringBuilder description = new StringBuilder("Venta de ").append(jewel.getName()).append(".\n Total= $").append(total.toString());
        if(quantity>1)
            description.append(".\nSe vendieron ").append(quantity).append(" Unidades.");
        else
            description.append(".\nSe vendio 1 Unidad.");
        
        return saveMovement(jewel, quantity, description.toString(), JewelMovementType.SALE);
    }

    @Override
    public Optional<JewelMovement> replacement(Jewel jewel, Long quantity) {
        StringBuilder description = new StringBuilder("Se repuso ").append(jewel.getName());
        if(quantity>1)
            description.append(".\nUn total de ").append(quantity).append(" Unidades.");
        else
            description.append(".\nUna unica unidad.");

        return saveMovement(jewel, quantity, description.toString(), JewelMovementType.REPLACEMENT);
    }

    public Optional<JewelMovement> saveMovement(Jewel jewel, Long quantity, String description, JewelMovementType type) {
        
        JewelMovement movement = new JewelMovement();
        movement.setDescription(description);
        movement.setJewel(jewel);
        movement.setType(type);
        movement.setQuantity(quantity);
        movement.setUser(userServiceHelper.getCurrentUser());
        
        return Optional.of(movementRepository.save(movement));
    }

    @Override
    public List<JewelMovement> findAll() {
        return movementRepository.findAllByOrderByTimestampDesc();
    }

    @Override
    public List<JewelMovement> findAllByType(JewelMovementType type) {
        return movementRepository.findAllByTypeOrderByTimestampDesc(type);
    }
}
