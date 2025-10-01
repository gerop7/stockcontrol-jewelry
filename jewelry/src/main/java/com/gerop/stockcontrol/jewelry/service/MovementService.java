package com.gerop.stockcontrol.jewelry.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Movement;
import com.gerop.stockcontrol.jewelry.model.entity.MovementType;
import com.gerop.stockcontrol.jewelry.repository.MovementRepository;

@Service
public class MovementService implements IMovementService {
    @Autowired
    public MovementRepository movementRepository;
    @Autowired
    public UserServiceHelper userServiceHelper;

    @Override
    public Optional<Movement> create(Jewel jewel) {
        Movement movement = new Movement();
        movement.setDescription("Articulo: "+jewel.getName() + " Creado exitosamente!");
        movement.setQuantity(jewel.getStock());
        movement.setJewel(jewel);
        movement.setType(MovementType.CREATE);
        movement.setUser(userServiceHelper.getCurrentUser());

        return Optional.of(movementRepository.save(movement));
    }

    @Override
    public Optional<Movement> modify(String modifyDescription, Jewel jewel) {
        Movement movement = new Movement();
        movement.setDescription(modifyDescription);
        movement.setJewel(jewel);
        movement.setType(MovementType.MODIFY);
        movement.setQuantity(0L);
        movement.setUser(userServiceHelper.getCurrentUser());

        return Optional.of(movementRepository.save(movement));
    }

    @Override
    public Optional<Movement> delete(Jewel jewel) {
        Movement movement = new Movement();
        movement.setDescription("Se elimino el articulo: "+jewel.getName());
        movement.setJewel(jewel);
        movement.setType(MovementType.DELETE);
        movement.setQuantity(0L);
        movement.setUser(userServiceHelper.getCurrentUser());

        return Optional.of(movementRepository.save(movement));
    }

    @Override
    public Optional<Movement> addStock(Jewel jewel, Long quantity) {
        Movement movement = new Movement();
        movement.setDescription("Se añadio "+quantity+" unidades de "+ jewel.getName()+ 
            ". Antes: "+(jewel.getStock()-quantity)+" unidades");
        movement.setJewel(jewel);
        movement.setType(MovementType.STOCK_ADD);
        movement.setQuantity(quantity);
        movement.setUser(userServiceHelper.getCurrentUser());

        return Optional.of(movementRepository.save(movement));
    }

    @Override
    public Optional<Movement> removeStock(Jewel jewel, Long quantity) {
        Movement movement = new Movement();
        movement.setDescription("Se removieron "+quantity+" unidades de "+ jewel.getName()+ 
            ". Antes: "+(jewel.getStock()+quantity)+" unidades");
        movement.setJewel(jewel);
        movement.setType(MovementType.STOCK_REMOVE);
        movement.setQuantity(quantity);
        movement.setUser(userServiceHelper.getCurrentUser());

        return Optional.of(movementRepository.save(movement));
    }

    @Override
    public Optional<Movement> sale(Jewel jewel, Long quantity) {
        Movement movement = new Movement();
        movement.setDescription((quantity==1)?"Se vendio: "+jewel.getName():"Se vendieron "+quantity+" unidades");
        movement.setJewel(jewel);
        movement.setType(MovementType.SALE);
        movement.setQuantity(quantity);
        movement.setUser(userServiceHelper.getCurrentUser());

        return Optional.of(movementRepository.save(movement));
    }
    

}
