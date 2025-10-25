package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.List;

import com.gerop.stockcontrol.jewelry.model.dto.sale.CompleteSaleDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.SaleListDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.SaleResultDto;

public interface ISaleService {
    SaleResultDto processSale(CompleteSaleDto sale);
    List<SaleListDto> listAll();
    List<SaleListDto> listAllAsc();
    List<SaleListDto> listAllFromInventory(Long inventoryId);
    List<SaleListDto> listAllFromInventoryAsc(Long inventoryId);
}
