package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingMetalRestock;
import com.gerop.stockcontrol.jewelry.repository.PendingMetalRestockRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PendingMetalRestockService implements IPendingRestockService<PendingMetalRestock,Float, Metal>{
    private final PendingMetalRestockRepository repository;

    public PendingMetalRestockService(PendingMetalRestockRepository repository) {
        this.repository = repository;
    }

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
    }

    @Override
    @Transactional
    public void removeFromRestock(PendingMetalRestock entity,Float quantity) {
        validateRestockOperation(entity, quantity);
        Float q = entity.getWeight() - quantity;
        entity.setWeight(q<0?0:q);
        save(entity);
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
                    },
                    () -> createSave(metal, inventory, quantity)
                );
        }
    }

    @Override
    @Transactional
    public void removeFromRestock(Long entityId, Long inventoryId, Float quantity) {
        PendingMetalRestock pending = repository.findByMetalIdAndInventoryId(entityId, inventoryId)
            .orElseThrow(() -> new EntityNotFoundException("No existe la reposicion"));
        removeFromRestock(pending, quantity);
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
