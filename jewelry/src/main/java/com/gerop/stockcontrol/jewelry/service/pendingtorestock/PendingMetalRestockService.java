package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingMetalRestock;
import com.gerop.stockcontrol.jewelry.repository.PendingRestockRepository;

@Service
public class PendingMetalRestockService implements IPendingRestockService<PendingMetalRestock,Float>{
    private final PendingRestockRepository pendingRestockRepository;

    public PendingMetalRestockService(PendingRestockRepository pendingRestockRepository) {
        this.pendingRestockRepository = pendingRestockRepository;
    }

    @Override
    public PendingMetalRestock create() {
        PendingMetalRestock pending = new PendingMetalRestock();
        pending.setWeight(0F);
        return pendingRestockRepository.save(pending);
    }

    @Override
    public void addToRestock(Long id,Float quantity) {
        PendingMetalRestock pending = (PendingMetalRestock)pendingRestockRepository.findById(id).orElseThrow();
        pending.setWeight(pending.getWeight()+quantity);
        pendingRestockRepository.save(pending);
    }

    @Override
    public void removeFromRestock(Long id,Float quantity) {
        PendingMetalRestock pending = (PendingMetalRestock)pendingRestockRepository.findById(id).orElseThrow();
        Float q = pending.getWeight() - quantity;
        pending.setWeight(q<0?0:q);
        pendingRestockRepository.save(pending);
    }   

}
