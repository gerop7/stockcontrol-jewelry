package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.dto.JewelFilterDto;
import org.springframework.data.domain.Page;

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
    Page<JewelDto> findAllByCurrentUserDto(int page, int size);
    Page<JewelDto> findAllByInventoryDto(Long inventoryId, int page, int size);
    Optional<Jewel> findById(Long id);
    Page<Jewel> findAllByCurrentUser(int page, int size);
    Page<Jewel> findAllByInventory(Long inventoryId, int page, int size);
    Optional<JewelDto> findByIdAndInventoryIdDto(Long id, Long inventoryId);
    Optional<Jewel> findByIdAndInventoryId(Long id, Long inventoryId);
    Page<JewelDto> filterMyJewels(JewelFilterDto f, int page, int size);
    Page<JewelDto> filterJewels(JewelFilterDto f, int page, int size);

    boolean haveStones(Long jewelId);
    boolean existsByIdAndHasOneMetal(Jewel jewel, Long metalId);
    boolean existsByIdAndHasOneStone(Jewel jewel, Long stoneId);
}
