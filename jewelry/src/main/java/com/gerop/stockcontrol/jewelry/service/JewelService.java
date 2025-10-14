package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.dto.JewelDto;
import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;
import com.gerop.stockcontrol.jewelry.repository.CategoryRepository;
import com.gerop.stockcontrol.jewelry.repository.MetalRepository;
import com.gerop.stockcontrol.jewelry.repository.JewelRepository;
import com.gerop.stockcontrol.jewelry.repository.SubcategoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IJewelService;
import com.gerop.stockcontrol.jewelry.service.movement.IJewelMovementService;

@Service
public class JewelService implements IJewelService{
    @Autowired
    private JewelRepository jewelRepository;

    @Autowired
    private IJewelMovementService movementService;

    @Autowired
    private UserServiceHelper userServiceHelper;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private MetalRepository metalRepository;


    @Override
    @Transactional
    public Optional<Jewel> create(JewelDto jewelDto) {
        if(jewelRepository.existByName(jewelDto.getName()))
            return Optional.empty();

        Jewel jewel = new Jewel(jewelDto.getName(), jewelDto.getDescription(), jewelDto.getStock());

        jewel.setUser(userServiceHelper.getCurrentUser());

        jewel.setImageUrl(jewelDto.getImageUrl());

        if (jewelDto.getCategoryId() == null || jewelDto.getSubcategoryId() == null || jewelDto.getMetalIds() == null || jewelDto.getMetalIds().isEmpty()) {
            return Optional.empty();
        }

        Category category = categoryRepository.findById(jewelDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Subcategory subcategory = subcategoryRepository.findById(jewelDto.getSubcategoryId()).orElseThrow(() -> new RuntimeException("Subategoría no encontrada"));
        if(!subcategory.getPrincipalCategory().getId().equals(category.getId()))
            throw new IllegalArgumentException("La subcategoría no pertenece a la categoría seleccionada");
        
        List<Metal> metal = metalRepository.findAllById(jewelDto.getMetalIds());

        jewel.setCategory(category);
        jewel.setSubcategory(subcategory);
        jewel.setMetal(metal);

        Jewel saved = jewelRepository.save(jewel);

        movementService.create(jewel);

        return Optional.of(saved);
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
        return jewelRepository.findById(id);
    }

    @Override
    public List<Jewel> findAll() {
    }

    @Override
    public Optional<Jewel> addStock(Long id, Long quantity) {
    }

    @Override
    public Optional<Jewel> sale(Long quantity, Long id, Long quantityToRestock) {
        
    }

    @Override
    public Optional<Jewel> removeStock(Long id, Long quantity) {
    }

}
