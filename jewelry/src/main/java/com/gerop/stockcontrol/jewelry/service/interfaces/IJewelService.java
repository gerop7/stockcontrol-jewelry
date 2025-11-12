package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.dto.JewelDto;
import com.gerop.stockcontrol.jewelry.model.dto.UpdateJewelDataDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.SaleJewel;

public interface IJewelService {
    JewelDto create(JewelDto jewelDto);
    Boolean delete(Long id);
    Jewel save(Jewel jewel);
    
    void update(Long id, UpdateJewelDataDto updateData);

    void addStock(Long id, Long inventoryId, Long quantity, String description);
    SaleJewel sale(Long id,Long quantity, Long quantityToRestock, Float total, Inventory inventory);
    void addToInventory(Long id, Long inventoryId, Long quantity);
    void removeFromInventory(Long id, Long inventoryId);

    void addPendingToRestock(Long jewelId, Long inventoryId, Long quantity);
    void removePendingToRestock(Long jewelId, Long inventoryId, Long quantity);

    Optional<JewelDto> findByIdDto(Long id);
    List<JewelDto> findAllDto();
    List<JewelDto> findAllByInventoryDto(Long inventoryId);
    Optional<Jewel> findById(Long id);
    List<Jewel> findAll();
    List<Jewel> findAllByInventory(Long inventoryId);

    boolean haveStones(Long jewelId);
    boolean existsByIdAndHasOneMetal(Jewel jewel, Long metalId);
    public boolean existsByIdAndHasOneStone(Jewel jewel, Long stoneId);
}
