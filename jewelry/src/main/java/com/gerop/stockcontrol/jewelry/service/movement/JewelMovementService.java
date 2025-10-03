package com.gerop.stockcontrol.jewelry.service.movement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.enums.JewelMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.JewelMovement;
import com.gerop.stockcontrol.jewelry.repository.JewelMovementRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

@Service
public class JewelMovementService implements IJewelMovementService {
    @Autowired
    public JewelMovementRepository movementRepository;
    @Autowired
    public UserServiceHelper userServiceHelper;

    @Override
    public Optional<JewelMovement> create(Jewel jewel) {
        JewelMovement movement = new JewelMovement();
        movement.setDescription("Articulo: "+jewel.getName() + " Creado exitosamente!");
        movement.setQuantity(jewel.getStock());
        movement.setJewel(jewel);
        movement.setType(JewelMovementType.CREATE);
        movement.setUser(userServiceHelper.getCurrentUser());

        return Optional.of(movementRepository.save(movement));
    }

    @Override
    public Optional<JewelMovement> modify(String modifyDescription, Jewel jewel) {
        JewelMovement movement = new JewelMovement();
        movement.setDescription(modifyDescription);
        movement.setJewel(jewel);
        movement.setType(JewelMovementType.MODIFY);
        movement.setQuantity(0L);
        movement.setUser(userServiceHelper.getCurrentUser());

        return Optional.of(movementRepository.save(movement));
    }

    @Override
    public Optional<JewelMovement> delete(Jewel jewel) {
        JewelMovement movement = new JewelMovement();
        movement.setDescription("Se elimino el articulo: "+jewel.getName());
        movement.setJewel(jewel);
        movement.setType(JewelMovementType.DELETE);
        movement.setQuantity(0L);
        movement.setUser(userServiceHelper.getCurrentUser());

        return Optional.of(movementRepository.save(movement));
    }

    @Override
    public Optional<JewelMovement> addStock(Jewel jewel, Long quantity) {
        JewelMovement movement = new JewelMovement();
        movement.setDescription("Se añadio "+quantity+" unidades de "+ jewel.getName()+ 
            ". Antes: "+(jewel.getStock()-quantity)+" unidades");
        movement.setJewel(jewel);
        movement.setType(JewelMovementType.STOCK_ADD);
        movement.setQuantity(quantity);
        movement.setUser(userServiceHelper.getCurrentUser());

        return Optional.of(movementRepository.save(movement));
    }

    @Override
    public Optional<JewelMovement> sale(Jewel jewel, Long quantity, Float total) {
        JewelMovement movement = new JewelMovement();
        StringBuilder description = new StringBuilder("Venta de ").append(jewel.getName()).append(".\n Total= $").append(total.toString());
        if(quantity>1)
            description.append(".\n Se vendieron ").append(quantity).append(" Unidades.");
        else
            description.append(".\n Se vendio 1 Unidad.");
        movement.setDescription(description.toString());
        movement.setJewel(jewel);
        movement.setType(JewelMovementType.SALE);
        movement.setQuantity(quantity);
        movement.setUser(userServiceHelper.getCurrentUser());

        return Optional.of(movementRepository.save(movement));
    }
}
