package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.StockNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.inventory.InventoryAccessDeniedException;
import com.gerop.stockcontrol.jewelry.exception.jewel.JewelNotFoundException;
import com.gerop.stockcontrol.jewelry.mapper.JewelMapper;
import com.gerop.stockcontrol.jewelry.model.dto.JewelDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateJewelDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.SaleJewel;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingJewelRestock;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.JewelryStockByInventory;
import com.gerop.stockcontrol.jewelry.repository.JewelRepository;
import com.gerop.stockcontrol.jewelry.repository.JewelryStockByInventoryRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.ICategoryService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IJewelService;
import com.gerop.stockcontrol.jewelry.service.interfaces.ISubcategoryService;
import com.gerop.stockcontrol.jewelry.service.movement.IJewelMovementService;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.IPendingRestockService;
import com.gerop.stockcontrol.jewelry.service.permissions.JewelPermissionsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JewelService implements IJewelService{

    private final JewelPermissionsService jewelPermissionsService;
    private final JewelRepository jewelRepository;
    private final IJewelMovementService movementService;
    private final UserServiceHelper userServiceHelper;
    private final IPendingRestockService<PendingJewelRestock, Long, Jewel> pendingRestockService;
    private final ICategoryService categoryService;
    private final ISubcategoryService subcategoryService;
    private final JewelryStockByInventoryRepository stockRepository;
    private final JewelMapper mapper;

    @Override
    @Transactional
    public JewelDto create(JewelDto jewelDto) {
        


        return null;
    }

    @Override
    public Boolean delete(Long id) {
        Jewel jewel = jewelRepository.findById(id).orElseThrow(()-> new JewelNotFoundException(id));
        jewel.setActive(false);

        save(jewel);

        return true;
    }

    @Override
    @Transactional
    public Jewel save(Jewel jewel) {
        return jewelRepository.save(jewel);
    }

    @Override
    public JewelDto update(Long id, UpdateJewelDataDto updateData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JewelDto addStock(Long id, Long inventoryId, Long quantity, String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public SaleJewel sale(Long jewelId, Long quantity, Long quantityToRestock, Float total, Inventory inventory) {
        Jewel jewel = jewelRepository.findByIdWithMaterials(jewelId).orElseThrow(()-> new JewelNotFoundException("La joya con "+jewelId+" no existe!"));
        if(!jewelPermissionsService.canModifyStock(jewelId, inventory.getId(), userServiceHelper.getCurrentUser().getId())){
            throw new InventoryAccessDeniedException("No estas autorizado a modificar el stock de la joya "+jewel.getSku()+" en el inventario "+inventory.getName());
        }
        if(quantity==null || quantity<=0){
            throw new InvalidQuantityException("La cantidad a vender de la joya "+jewel.getSku()+" debe ser mayor que 0.");
        }

        JewelryStockByInventory jewelryStock = stockRepository.findByJewelIdAndInventoryId(jewelId, inventory.getId())
            .orElseThrow(() -> new StockNotFoundException("No se encontró stock para la joya"+ jewel.getSku()+"en el inventario"+inventory.getName()+"."));
        
        Long q = (quantity>jewelryStock.getStock())?0L:(jewelryStock.getStock()-quantity);
        jewelryStock.setStock(q);
        stockRepository.save(jewelryStock);

        handleAddPendingToRestock(jewel, inventory, quantityToRestock);

        movementService.sale(jewel, quantity, total, inventory);

        return new SaleJewel(jewel, quantity, total);
    }

    @Override
    public Optional<JewelDto> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<JewelDto> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    @Transactional(readOnly=true)
    public boolean haveStones(Long jewelId){
        return jewelRepository.existsByIdAndHasStones(jewelId);
    }
    
    @Override
    public void addPendingToRestock(Long jewelId, Long inventoryId, Long quantity) {
        throw new UnsupportedOperationException("Unimplemented method 'addPendingToRestock'");
    }
    
    @Transactional
    public void handleAddPendingToRestock(Jewel jewel, Inventory inventory, Long quantity) {
        if(quantity!= null && quantity>0){
            if(pendingRestockService.existsByInventory(jewel.getId(),inventory.getId())){
                pendingRestockService.addToRestock(jewel, inventory, quantity);
            }else{
                jewel.getPendingRestock().add(pendingRestockService.createSave(jewel, inventory, quantity));
            }
        }
    }

    @Override
    public void removePendingToRestock(Jewel jewel, Inventory inventory, Long quantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean existsByIdAndHasOneMetal(Jewel jewel, Long metalId){
        boolean hasMetal = jewel
            .getMetal().stream()
            .anyMatch(m -> m.getId().equals(metalId));
        
        return hasMetal;
    }

    @Override
    @Transactional(readOnly=true)
    public boolean existsByIdAndHasOneStone(Jewel jewel, Long stoneId){
        boolean hasStone = jewel
            .getStone().stream()
            .anyMatch(s -> s.getId().equals(stoneId));

        return hasStone;
    }

}
