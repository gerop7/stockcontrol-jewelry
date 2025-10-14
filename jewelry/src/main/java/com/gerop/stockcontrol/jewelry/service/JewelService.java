package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.exception.CategoryRequiredException;
import com.gerop.stockcontrol.jewelry.model.dto.JewelDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateJewelDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingJewelRestock;
import com.gerop.stockcontrol.jewelry.repository.CategoryRepository;
import com.gerop.stockcontrol.jewelry.repository.JewelRepository;
import com.gerop.stockcontrol.jewelry.repository.SubcategoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IJewelService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMetalService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IStoneService;
import com.gerop.stockcontrol.jewelry.service.movement.IJewelMovementService;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.IPendingRestockService;

import jakarta.transaction.Transactional;

@Service
public class JewelService implements IJewelService{

    private final JewelRepository jewelRepository;
    private final IJewelMovementService movementService;
    private final UserServiceHelper userServiceHelper;
    private final IPendingRestockService<PendingJewelRestock, Long> pendingRestockService;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final IMetalService metalService;
    private final IStoneService stoneService;

    public JewelService(CategoryRepository categoryRepository, JewelRepository jewelRepository, 
        IMetalService metalService, IJewelMovementService movementService, 
        IPendingRestockService<PendingJewelRestock, Long> pendingRestockService, IStoneService stoneService,
        SubcategoryRepository subcategoryRepository, UserServiceHelper userServiceHelper){
            this.categoryRepository = categoryRepository;
            this.jewelRepository = jewelRepository;
            this.metalService = metalService;
            this.movementService = movementService;
            this.pendingRestockService = pendingRestockService;
            this.stoneService = stoneService;
            this.subcategoryRepository = subcategoryRepository;
            this.userServiceHelper = userServiceHelper;
    }
    
    

    @Override
    @Transactional
    public Jewel create(JewelDto jewelDto) {
        Jewel jewel = new Jewel(jewelDto.getName(), jewelDto.getDescription(), jewelDto.getSku(),
            jewelDto.getStock(), jewelDto.getImageUrl(), jewelDto.getWeight(),
            jewelDto.getSize(), userServiceHelper.getCurrentUser()
        );

        jewel.setPendingRestock(pendingRestockService.create());

        if (jewelDto.getCategoryId()!=null  && jewelDto.getCategoryId()>0) {
            Category category = categoryRepository.findById(jewelDto.getCategoryId()).orElseThrow(()-> new IllegalArgumentException("Category not found"));
            if(!category.getUser().getId().equals(userServiceHelper.getCurrentUser().getId())){
                throw new IllegalArgumentException("Category not found");
            }
            jewel.setCategory(category);

            if(jewelDto.getSubcategoryId()>0){
                Subcategory subcategory = subcategoryRepository.findById(jewelDto.getSubcategoryId()).orElseThrow(()-> new IllegalArgumentException("Subcategory not found"));
                if(!subcategory.getUser().getId().equals(userServiceHelper.getCurrentUser().getId()) || !subcategory.getPrincipalCategory().getId().equals(category.getId)){
                    throw new IllegalArgumentException("Subcategory not found");
                }
                jewel.setSubcategory(subcategory);
            }
        }else{
            throw new CategoryRequiredException;
        }
    }

    @Override
    public Boolean delete(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Jewel save(Jewel jewel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JewelDto update(Long id, UpdateJewelDataDto updateData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JewelDto addStock(Long id, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JewelDto sale(Long id, Long quantity, Long quantityToRestock) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Jewel> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Jewel> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
