package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingStoneRestock;
import com.gerop.stockcontrol.jewelry.repository.PendingRestockRepository;

@Service
public class PendingStoneRestockService implements IPendingRestockService<PendingStoneRestock,Long>{

    private final PendingRestockRepository pendingRestockRepository;

    public PendingStoneRestockService(PendingRestockRepository pendingRestockRepository) {
        this.pendingRestockRepository = pendingRestockRepository;
    }

    @Override
    public PendingStoneRestock create() {
        PendingStoneRestock pending= new PendingStoneRestock();
        pending.setQuantity(0L);
        return save(pending);
    }

    @Override
    public void addToRestock(Long id,Long quantity) {
        PendingStoneRestock pending = (PendingStoneRestock)pendingRestockRepository.findById(id).orElseThrow();
        pending.setQuantity(pending.getQuantity()+quantity);
        save(pending);
    }

    @Override
    public void removeFromRestock(Long id,Long quantity) {
        PendingStoneRestock pending = (PendingStoneRestock)pendingRestockRepository.findById(id).orElseThrow();
        Long q = pending.getQuantity() - quantity;
        pending.setQuantity(q<0?0:q);
        save(pending);
    }

    @Override
    public PendingStoneRestock save(PendingStoneRestock entity) {
        return pendingRestockRepository.save(entity);
    }


}
