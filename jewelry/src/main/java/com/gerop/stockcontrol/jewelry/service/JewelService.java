package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.model.dto.JewelDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateJewelDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
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

import jakarta.persistence.EntityNotFoundException;
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
        Jewel jewel = new Jewel(
            jewelDto.getName(), 
            jewelDto.getDescription(), 
            jewelDto.getSku(),
            jewelDto.getImageUrl(), 
            jewelDto.getWeight(),
            jewelDto.getSize(), 
            userServiceHelper.getCurrentUser()
        );

        Long userId = userServiceHelper.getCurrentUser().getId();

        
        if (jewelDto.getCategoryId()!=null  && jewelDto.getCategoryId()>0) {
            Category category = categoryRepository.findByIdAndUserId(jewelDto.getCategoryId(), userId)
            .orElseThrow(()-> new EntityNotFoundException("Category not found"));

            jewel.setCategory(category);

            if(jewelDto.getSubcategoryId()!=null){
                Subcategory subcategory = subcategoryRepository.findByIdAndUserId(jewelDto.getSubcategoryId(), userId)
                .orElseThrow(()-> new EntityNotFoundException("Subcategory not found"));

                if(!subcategory.getPrincipalCategory().getId().equals(category.getId())){
                    throw new RequiredFieldException("Subcategory does not belong to the selected category");
                }
                jewel.setSubcategory(subcategory);
            }
            
        }else{
            throw new RequiredFieldException("Category is required!");
        }


        List<Long> metalIds = jewelDto.getMetalIds();
        if (metalIds ==null || metalIds.isEmpty()) {
            throw new RequiredFieldException("Metal is required!");
        }

        List<Metal> metals = metalService.findAllById(metalIds);
        if (metals.size() != metalIds.size()) {
            throw new EntityNotFoundException("One or more metals not found");
        }
        jewel.setMetal(metals);


        List<Long> stoneIds = jewelDto.getStoneIds();
        if (stoneIds!=null && !stoneIds.isEmpty()) {
            List<Stone> stones = stoneService.findAllById(stoneIds);
            if (stones.size()!=stoneIds.size()) {
                throw new EntityNotFoundException("One or more stones not found");
            }
            jewel.setStone(stones);
        }


        jewel.setPendingRestock(pendingRestockService.create());

        Jewel saved = save(jewel);
        movementService.create(jewel);

        return saved;
    }

    @Override
    public Boolean delete(Long id) {
        Jewel jewel = jewelRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Jewel not found"));
        jewel.setActive(false);

        save(jewel);

        return true;
    }

    @Override
    public Jewel save(Jewel jewel) {
        return jewelRepository.save(jewel);
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
