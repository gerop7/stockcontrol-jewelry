package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingJewelRestock;
import com.gerop.stockcontrol.jewelry.repository.PendingJewelRestockRepository;
import com.gerop.stockcontrol.jewelry.service.movement.IJewelMovementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PendingJewelRestockService implements IPendingRestockService<PendingJewelRestock, Long, Jewel>{

    private final PendingJewelRestockRepository repository;
    private final IJewelMovementService movementService;


    @Override
    @Transactional
    public PendingJewelRestock create(Jewel jewel, Inventory inventory) {
        return create(jewel, inventory, 0L);
    }

    @Override
    @Transactional
    public PendingJewelRestock create(Jewel jewel, Inventory inventory, Long quantity) {
        Objects.requireNonNull(inventory, "Inventory cannot be null");
        Objects.requireNonNull(jewel, "Jewel cannot be null");
        Objects.requireNonNull(quantity, "Quantity cannot be null");

        return repository.findByJewelIdAndInventoryId(jewel.getId(), inventory.getId())
            .orElseGet(() -> {
                PendingJewelRestock pending = new PendingJewelRestock();
                pending.setQuantity(quantity);
                pending.setInventory(inventory);
                pending.setJewel(jewel);
                return pending;
            });
    }

    @Transactional
    @Override
    public PendingJewelRestock createSave(Jewel jewel, Inventory inventory) {
        return save(create(jewel,inventory));
    }

    @Transactional
    @Override
    public PendingJewelRestock createSave(Jewel jewel, Inventory inventory, Long quantity) {
        return save(create(jewel,inventory,quantity));
    }

    @Transactional
    @Override
    public PendingJewelRestock save(PendingJewelRestock entity) {
        return repository.save(entity);
    }
    
    @Transactional
    @Override
    public void addToRestock(PendingJewelRestock entity,Long quantity) {
        validateRestockOperation(entity, quantity);
        entity.setQuantity(entity.getQuantity() + quantity);
        save(entity);
        movementService.marked_replacement(entity.getJewel(),quantity, entity.getInventory());
    }

    @Transactional
    @Override
    public void removeFromRestock(PendingJewelRestock entity,Long quantity) {
        validateRestockOperation(entity, quantity);
        long q = entity.getQuantity() - quantity;
        entity.setQuantity(q < 0 ? 0 : q);
        save(entity);
        movementService.replacement(entity.getJewel(),quantity, entity.getInventory());
    }

    @Transactional
    @Override
    public void addToRestock(Jewel jewel, Inventory inventory, Long quantity) {
        if(inventory!=null && jewel!=null){
            if(quantity==null || quantity<0)
                throw new InvalidQuantityException("La cantidad a reponer de la joya "+jewel.getSku()+" es invalida!");
            if(quantity>0){
                repository.findByJewelIdAndInventoryId(jewel.getId(), inventory.getId())
                    .ifPresentOrElse(
                        p->{
                            p.setQuantity(p.getQuantity()+quantity);
                            save(p);
                            movementService.marked_replacement(jewel, quantity, inventory);
                        },
                        () -> createSave(jewel, inventory, quantity)
                    );
            }
        }
    }

    @Transactional
    @Override
    public void removeFromRestock(Jewel jewel, Inventory inventory, Long quantity) {
        if(inventory!=null && jewel!=null){
            if(quantity==null || quantity<0)
                throw new InvalidQuantityException("La cantidad a reponer de la joya "+jewel.getSku()+" es invalida!");
            if(quantity>0){
                repository.findByJewelIdAndInventoryId(jewel.getId(), inventory.getId())
                    .ifPresent(
                        p->{
                            long q = p.getQuantity() - quantity;
                            if (q <= 0) {
                                repository.delete(p);
                            } else {
                                p.setQuantity(q);
                                save(p);
                            }
                            movementService.replacement(jewel, quantity, inventory);
                        }
                    );
            }
        }else
            throw new RequiredFieldException("Es necesario que se completen todos los campos!");
    }

    public void validateRestockOperation(PendingJewelRestock entity, Long quantity) {
        if (entity == null)
            throw new RequiredFieldException("PendingJewelRestock cannot be null");
        if (quantity == null || quantity <= 0)
            throw new InvalidQuantityException("Quantity must be greater than zero");
    }

    @Override
    public boolean existsByInventory(Long jewelId, Long inventoryId){
        return repository.existsByJewelIdAndInventoryId(jewelId, inventoryId);
    }

    @Override
    public void remove(Jewel object, Inventory inventory) {
        repository.findByJewelIdAndInventoryId(object.getId(), inventory.getId()).ifPresent(repository::delete);
    }

    @Override
    public Optional<PendingJewelRestock> findOne(Jewel object, Inventory inventory) {
        return repository.findByJewelIdAndInventoryId(object.getId(), inventory.getId());
    }
}
