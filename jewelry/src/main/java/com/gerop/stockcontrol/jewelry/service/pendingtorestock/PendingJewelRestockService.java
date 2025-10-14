package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingJewelRestock;
import com.gerop.stockcontrol.jewelry.repository.PendingRestockRepository;

@Service
public class PendingJewelRestockService implements IPendingRestockService<PendingJewelRestock, Long>{

    private final PendingRestockRepository pendingRestockRepository;

    public PendingJewelRestockService(PendingRestockRepository pendingRestockRepository) {
        this.pendingRestockRepository = pendingRestockRepository;
    }

    @Override
    public PendingJewelRestock create() {
        PendingJewelRestock pendingJewelRestock = new PendingJewelRestock();
        pendingJewelRestock.setQuantity(0L);
        return pendingRestockRepository.save(pendingJewelRestock);
    }

    @Override
    public void addToRestock(Long id,Long quantity) {
        PendingJewelRestock pendingJewelRestock = (PendingJewelRestock)pendingRestockRepository.findById(id).orElseThrow();
        pendingJewelRestock.setQuantity(pendingJewelRestock.getQuantity() + quantity);
        pendingRestockRepository.save(pendingJewelRestock);
    }

    @Override
    public void removeFromRestock(Long id,Long quantity) {
        PendingJewelRestock pendingJewelRestock = (PendingJewelRestock)pendingRestockRepository.findById(id).orElseThrow();
        Long q = pendingJewelRestock.getQuantity() - quantity;
        pendingJewelRestock.setQuantity(q < 0 ? 0 : q);
        pendingRestockRepository.save(pendingJewelRestock);
    }

}
