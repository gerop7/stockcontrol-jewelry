package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.model.dto.JewelDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateJewelDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Metal;
import com.gerop.stockcontrol.jewelry.model.entity.SaleJewel;
import com.gerop.stockcontrol.jewelry.model.entity.Stone;
import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingJewelRestock;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;
import com.gerop.stockcontrol.jewelry.repository.CategoryRepository;
import com.gerop.stockcontrol.jewelry.repository.InventoryRepository;
import com.gerop.stockcontrol.jewelry.repository.JewelRepository;
import com.gerop.stockcontrol.jewelry.repository.JewelryStockByInventoryRepository;
import com.gerop.stockcontrol.jewelry.repository.SubcategoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IJewelService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMetalService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IStoneService;
import com.gerop.stockcontrol.jewelry.service.movement.IJewelMovementService;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.IPendingRestockService;
import com.gerop.stockcontrol.jewelry.service.permissions.JewelPermissionsService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class JewelService implements IJewelService{

    private final JewelPermissionsService jewelPermissionsService;
    private final JewelRepository jewelRepository;
    private final IJewelMovementService movementService;
    private final UserServiceHelper userServiceHelper;
    private final IPendingRestockService<PendingJewelRestock, Long, Jewel> pendingRestockService;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final IMetalService metalService;
    private final IStoneService stoneService;
    private final JewelryStockByInventoryRepository stockRepository;
    private final InventoryRepository inventoryRepository;

    
    
    

    public JewelService(JewelPermissionsService jewelPermissionsService, JewelRepository jewelRepository,
            IJewelMovementService movementService, UserServiceHelper userServiceHelper,
            IPendingRestockService<PendingJewelRestock, Long, Jewel> pendingRestockService,
            CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository,
            IMetalService metalService, IStoneService stoneService, JewelryStockByInventoryRepository stockRepository,
            InventoryRepository inventoryRepository) {
        this.jewelPermissionsService = jewelPermissionsService;
        this.jewelRepository = jewelRepository;
        this.movementService = movementService;
        this.userServiceHelper = userServiceHelper;
        this.pendingRestockService = pendingRestockService;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.metalService = metalService;
        this.stoneService = stoneService;
        this.stockRepository = stockRepository;
        this.inventoryRepository = inventoryRepository;
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
    @Transactional
    public SaleJewel sale(Long jewelId, Long quantity, Long quantityToRestock, Float total, Long inventoryId) {
        Jewel jewel = jewelRepository.findById(jewelId).orElseThrow(()-> new EntityNotFoundException("La joya con "+id+" no existe!"));
        if(!jewelPermissionsService.canModifyStock(jewelId, inventoryId, userServiceHelper.getCurrentUser().getId())){
            throw new SecurityException("No estas autorizado a modificar el stock de la joya "+jewel.getSku()+" en este");
        }
        if(quantity==null || quantity<=0){
            throw new IllegalArgumentException("La cantidad a vender de la joya "+jewel.getSku()+" debe ser mayor que 0.");
        }

        JewelryStockByInventory jewelryStock = stockRepository.findByJewelIdAndInventoryId(jewelId, inventoryId).get();
        
        Long q = (quantity>jewelryStock.getStock())?0L:(jewelryStock.getStock()-quantity);
        jewelryStock.setStock(q);

        stockRepository.save(jewelryStock);

        if(quantityToRestock!= null && quantityToRestock>0){
            if(pendingRestockService.existsByInventory(jewelId,inventoryId)){
                pendingRestockService.addToRestock(jewelId, inventoryId, quantityToRestock);
            }else{
                Inventory inventory = inventoryRepository.findById(inventoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado."));
                jewel.getPendingRestock().add(pendingRestockService.createSave(jewel, inventory, quantityToRestock));
            }
        }

        return new SaleJewel(jewel, quantity, total);
    }

    @Override
    public Optional<Jewel> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Jewel> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    @Transactional(readOnly=true)
    public boolean haveStones(Long jewelId){
        return jewelRepository.existsByIdAndHasStones(jewelId);
    }
}
