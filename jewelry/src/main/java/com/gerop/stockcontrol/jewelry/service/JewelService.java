package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.dto.JewelDto;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.repository.JewelRepository;

@Service
public class JewelService implements IJewelService{

    private JewelRepository jewelRepository;
    private IMovementService movementService;

    @Override
    public Optional<Jewel> create(JewelDto jewelDto) {
        
    }

    @Override
    public Optional<Jewel> updateName(Long id, String name) {
    }

    @Override
    public Optional<Jewel> updateDescription(Long id, String description) {
    }

    @Override
    public Optional<Jewel> remove(Long id) {
    }

    @Override
    public Optional<Jewel> findById(Long id) {
    }

    @Override
    public List<Jewel> findAll() {
    }

    @Override
    public Optional<Jewel> addStock(Long id, Long quantity) {
    }

    @Override
    public Optional<Jewel> removeStock(Long id, Long quantity) {
    }

}
