package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import java.util.Objects;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.service.movement.StoneMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingStoneRestock;
import com.gerop.stockcontrol.jewelry.repository.PendingStoneRestockRepository;

@Service
@RequiredArgsConstructor
public class PendingStoneRestockService implements IPendingRestockService<PendingStoneRestock,Long, Stone>{

    private final PendingStoneRestockRepository repository;
    private final StoneMovementService movementService;

    @Override
    @Transactional
    public PendingStoneRestock create(Stone stone, Inventory inventory) {
        return create(stone, inventory, 0L);
    }
    
    @Override
    @Transactional
    public PendingStoneRestock createSave(Stone stone, Inventory inventory) {
        return repository.save(create(stone, inventory));
    }
    @Override
    @Transactional
    public PendingStoneRestock create(Stone entity, Inventory inventory, Long quantity) {
        Objects.requireNonNull(inventory, "Inventory cannot be null");
        Objects.requireNonNull(entity, "Stone cannot be null");
        Objects.requireNonNull(quantity, "Quantity cannot be null");

        return repository.findByStoneIdAndInventoryId(entity.getId(), inventory.getId())
            .orElseGet(() -> {
                PendingStoneRestock pending = new PendingStoneRestock();
                pending.setQuantity(quantity);
                pending.setInventory(inventory);
                pending.setStone(entity);
                return pending;
            });
    }

    @Override
    public PendingStoneRestock createSave(Stone entity, Inventory inventory, Long quantity) {
        return repository.save(create(entity, inventory, quantity));
    }
    
    @Override
    @Transactional
    public PendingStoneRestock save(PendingStoneRestock entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void addToRestock(PendingStoneRestock entity, Long quantity) {
        validateRestockOperation(entity, quantity);
        entity.setQuantity(entity.getQuantity()+quantity);
        save(entity);
        movementService.marked_replacement(entity.getStone(),quantity, entity.getInventory());
    }

    @Override
    @Transactional
    public void removeFromRestock(PendingStoneRestock entity, Long quantity) {
        validateRestockOperation(entity, quantity);
        long q = entity.getQuantity() - quantity;
        entity.setQuantity(q<0?0:q);
        save(entity);
        movementService.replacement(entity.getStone(),quantity, entity.getInventory());
    }

    @Override
    @Transactional
    public void addToRestock(Stone stone, Inventory inventory, Long quantity) {
        if(inventory!=null && stone!=null && quantity!=null){
            repository.findByStoneIdAndInventoryId(stone.getId(), inventory.getId())
                .ifPresentOrElse(
                    p->{
                        p.setQuantity(p.getQuantity()+quantity);
                        save(p);
                        movementService.marked_replacement(stone, quantity, inventory);
                    },
                    () -> createSave(stone, inventory, quantity)
                );
        }
    }

    @Override
    @Transactional
    public void removeFromRestock(Stone stone, Inventory inventory, Long quantity) {
        if(inventory!=null &&stone!=null){
            if(quantity==null || quantity<0)
                throw new InvalidQuantityException("La cantidad a reponer de la joya "+stone.getName()+" es invalida!");
            if(quantity>0){
                repository.findByStoneIdAndInventoryId(stone.getId(), inventory.getId())
                        .ifPresent(
                                p->{
                                    long q = p.getQuantity() - quantity;
                                    if (q <= 0) {
                                        repository.delete(p);
                                    } else {
                                        p.setQuantity(q);
                                        save(p);
                                    }
                                    movementService.replacement(stone, quantity, inventory);
                                }
                        );
            }
        }else
            throw new RequiredFieldException("Es necesario que se completen todos los campos!");
    }

    private void validateRestockOperation(PendingStoneRestock entity, Long quantity) {
        if (entity == null)
            throw new IllegalArgumentException("PendingStoneRestock cannot be null");
        if (quantity == null || quantity <= 0)
            throw new IllegalArgumentException("Quantity must be greater than zero");
    }

    @Override
    public boolean existsByInventory(Long stoneId, Long inventoryId){
        return repository.existsByStoneIdAndInventoryId(stoneId, inventoryId);
    }

    @Override
    public void remove(Stone object, Inventory inventory) {
        repository.findByStoneIdAndInventoryId(object.getId(), inventory.getId()).ifPresent(repository::delete);
    }

    @Override
    public Optional<PendingStoneRestock> findOne(Stone object, Inventory inventory) {
        return repository.findByStoneIdAndInventoryId(object.getId(), inventory.getId());
    }
}
