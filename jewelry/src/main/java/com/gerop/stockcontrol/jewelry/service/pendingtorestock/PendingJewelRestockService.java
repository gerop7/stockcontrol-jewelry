package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingJewelRestock;
import com.gerop.stockcontrol.jewelry.repository.PendingJewelRestockRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PendingJewelRestockService implements IPendingRestockService<PendingJewelRestock, Long, Jewel>{

    private final PendingJewelRestockRepository repository;
    
    public PendingJewelRestockService(PendingJewelRestockRepository repository) {
        this.repository = repository;
    }

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
    }

    @Transactional
    @Override
    public void removeFromRestock(PendingJewelRestock entity,Long quantity) {
        validateRestockOperation(entity, quantity);
        Long q = entity.getQuantity() - quantity;
        entity.setQuantity(q < 0 ? 0 : q);
        save(entity);
    }

    @Transactional
    @Override
    public void addToRestock(Jewel jewel, Inventory inventory, Long quantity) {
        if(inventory!=null && jewel!=null && quantity!=null){
            repository.findByJewelIdAndInventoryId(jewel.getId(), inventory.getId())
                .ifPresentOrElse(
                    p->{
                        p.setQuantity(p.getQuantity()+quantity);
                        save(p);
                    },
                    () -> createSave(jewel, inventory, quantity)
                );
        }
    }

    @Transactional
    @Override
    public void removeFromRestock(Long jewelId, Long inventoryId, Long quantity) {
        PendingJewelRestock pending = repository.findByJewelIdAndInventoryId(jewelId, inventoryId)
            .orElseThrow(() -> new EntityNotFoundException("No existe la reposicion"));
        removeFromRestock(pending, quantity);
    }

    public void validateRestockOperation(PendingJewelRestock entity, Long quantity) {
        if (entity == null)
            throw new IllegalArgumentException("PendingJewelRestock cannot be null");
        if (quantity == null || quantity <= 0)
            throw new IllegalArgumentException("Quantity must be greater than zero");
    }

    @Override
    public boolean existsByInventory(Long jewelId, Long inventoryId){
        return repository.existsByJewelIdAndInventoryId(jewelId, inventoryId);
    }
}
