package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingStoneRestock;
import com.gerop.stockcontrol.jewelry.repository.PendingStoneRestockRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PendingStoneRestockService implements IPendingRestockService<PendingStoneRestock,Long, Stone>{

    private final PendingStoneRestockRepository repository;

    public PendingStoneRestockService(PendingStoneRestockRepository repository) {
        this.repository = repository;
    }

    @Override
    public PendingStoneRestock create(Stone stone, Inventory inventory) {
        Objects.requireNonNull(inventory, "Inventory cannot be null");
        Objects.requireNonNull(stone, "Stone cannot be null");

        return repository.findByStoneIdAndInventoryId(stone.getId(), inventory.getId())
            .orElseGet(() -> {
                PendingStoneRestock pending = new PendingStoneRestock();
                pending.setQuantity(0L);
                pending.setInventory(inventory);
                pending.setStone(stone);
                return pending;
            });
    }

    @Override
    @Transactional
    public PendingStoneRestock createSave(Stone stone, Inventory inventory) {
        return repository.save(create(stone, inventory));
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
    }

    @Override
    @Transactional
    public void removeFromRestock(PendingStoneRestock entity, Long quantity) {
        validateRestockOperation(entity, quantity);
        Long q = entity.getQuantity() - quantity;
        entity.setQuantity(q<0?0:q);
        save(entity);
    }

    @Override
    @Transactional
    public void addToRestock(Long entityId, Long inventoryId, Long quantity) {
        PendingStoneRestock pending = repository.findByStoneIdAndInventoryId(entityId, inventoryId)
            .orElseThrow(() -> new EntityNotFoundException("No existe la reposicion"));
        addToRestock(pending, quantity);
    }

    @Override
    @Transactional
    public void removeFromRestock(Long entityId, Long inventoryId, Long quantity) {
        PendingStoneRestock pending = repository.findByStoneIdAndInventoryId(entityId, inventoryId)
            .orElseThrow(() -> new EntityNotFoundException("No existe la reposicion"));
        removeFromRestock(pending, quantity);
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
}
