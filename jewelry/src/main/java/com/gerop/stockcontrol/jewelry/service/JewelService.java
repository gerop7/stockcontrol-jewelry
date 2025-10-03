package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.dto.JewelDto;
import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.model.entity.Composition;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;
import com.gerop.stockcontrol.jewelry.repository.CategoryRepository;
import com.gerop.stockcontrol.jewelry.repository.CompositionRepository;
import com.gerop.stockcontrol.jewelry.repository.JewelRepository;
import com.gerop.stockcontrol.jewelry.repository.SubcategoryRepository;
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
    private CompositionRepository compositionRepository;


    @Override
    public Optional<Jewel> create(JewelDto jewelDto) {
        if(jewelRepository.existByName(jewelDto.getName()))
            return Optional.empty();

        Jewel jewel = new Jewel(jewelDto.getName(), jewelDto.getDescription(), jewelDto.getStock());

        jewel.setUser(userServiceHelper.getCurrentUser());

        jewel.setImageUrl(jewelDto.getImageUrl());

        if (jewelDto.getCategoryId() == null || jewelDto.getSubcategoryId() == null || jewelDto.getCompositionIds() == null || jewelDto.getCompositionIds().isEmpty()) {
            return Optional.empty();
        }

        Category category = categoryRepository.findById(jewelDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Subcategory subcategory = subcategoryRepository.findById(jewelDto.getSubcategoryId()).orElseThrow(() -> new RuntimeException("Subategoría no encontrada"));
        if(subcategory.getPrincipalCategory().getId().equals(category.getId()))
            throw new IllegalArgumentException("La subcategoría no pertenece a la categoría seleccionada");
        
        List<Composition> compositions = compositionRepository.findAllById(jewelDto.getCompositionIds());

        jewel.setCategory(category);
        jewel.setSubcategory(subcategory);
        jewel.setComposition(compositions);

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
