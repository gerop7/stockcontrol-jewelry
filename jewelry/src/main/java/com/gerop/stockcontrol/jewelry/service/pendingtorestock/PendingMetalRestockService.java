package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import java.util.Objects;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.service.movement.MetalMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingMetalRestock;
import com.gerop.stockcontrol.jewelry.repository.PendingMetalRestockRepository;


@Service
@RequiredArgsConstructor
public class PendingMetalRestockService implements IPendingRestockService<PendingMetalRestock,Float, Metal>{
    private final PendingMetalRestockRepository repository;
    private final MetalMovementService movementService;

    @Override
    @Transactional
    public PendingMetalRestock create(Metal metal, Inventory inventory) {
        return create(metal, inventory, 0f);
    }

    @Override
    @Transactional
    public PendingMetalRestock create(Metal metal, Inventory inventory, Float quantity) {
        Objects.requireNonNull(inventory, "Inventory cannot be null");
        Objects.requireNonNull(metal, "Metal cannot be null");
        Objects.requireNonNull(quantity,"Quantity cannot be null");

        return repository.findByMetalIdAndInventoryId(metal.getId(), inventory.getId())
            .orElseGet(() -> {
                PendingMetalRestock pending = new PendingMetalRestock();
                pending.setWeight(quantity);
                pending.setInventory(inventory);
                pending.setMetal(metal);
                return pending;
            });
    }
    
    @Override
    @Transactional
    public PendingMetalRestock createSave(Metal metal,Inventory inventory) {
        return save(create(metal, inventory));
    }

    @Override
    @Transactional
    public PendingMetalRestock createSave(Metal entity, Inventory inventory, Float quantity) {
        return save(create(entity, inventory,quantity));
    }
    
    @Override
    @Transactional
    public PendingMetalRestock save(PendingMetalRestock entity) {
        return repository.save(entity);
    }


    @Override
    @Transactional
    public void addToRestock(PendingMetalRestock entity,Float quantity) {
        validateRestockOperation(entity, quantity);
        entity.setWeight(entity.getWeight()+quantity);
        save(entity);
        movementService.marked_replacement(entity.getMetal(),quantity,entity.getInventory());
    }

    @Override
    @Transactional
    public void removeFromRestock(PendingMetalRestock entity,Float quantity) {
        validateRestockOperation(entity, quantity);
        float q = entity.getWeight() - quantity;
        entity.setWeight(q<0?0:q);
        save(entity);
        movementService.replacement(entity.getMetal(),quantity,entity.getInventory());
    }   


    @Override
    @Transactional
    public void addToRestock(Metal metal, Inventory inventory, Float quantity) {
        if(inventory!=null && metal!=null && quantity!=null){
            repository.findByMetalIdAndInventoryId(metal.getId(), inventory.getId())
                .ifPresentOrElse(
                    p->{
                        p.setWeight(p.getWeight()+quantity);
                        save(p);
                        movementService.marked_replacement(metal,quantity,inventory);
                    },
                    () -> createSave(metal, inventory, quantity)
                );
        }
    }

    @Override
    @Transactional
    public void removeFromRestock(Metal metal, Inventory inventory, Float quantity) {
        if(inventory!=null && metal!=null){
            if(quantity==null || quantity<0)
                throw new InvalidQuantityException("La cantidad a reponer del metal "+metal.getName()+" es invalida!");
            if(quantity>0){
                repository.findByMetalIdAndInventoryId(metal.getId(), inventory.getId())
                        .ifPresent(
                                p->{
                                    float q = p.getWeight() - quantity;
                                    if (q <= 0) {
                                        repository.delete(p);
                                    } else {
                                        p.setWeight(q);
                                        save(p);
                                    }
                                    movementService.replacement(metal, p.getWeight(), inventory);
                                }
                        );
            }
        }else
            throw new RequiredFieldException("Es necesario que se completen todos los campos!");
    }

    private void validateRestockOperation(PendingMetalRestock entity, Float quantity) {
        if (entity == null)
            throw new IllegalArgumentException("PendingMetalRestock cannot be null");
        if (quantity == null || quantity <= 0)
            throw new IllegalArgumentException("Quantity must be greater than zero");
    }

    @Override
    public boolean existsByInventory(Long metalId, Long inventoryId){
        return repository.existsByMetalIdAndInventoryId(metalId, inventoryId);
    }

    @Transactional(readOnly=true)
    public Optional<PendingMetalRestock> findByMetalIdAndInventoryId(Long metalId, Long inventoryId){
        return repository.findByMetalIdAndInventoryId(metalId, inventoryId);
    }

}
