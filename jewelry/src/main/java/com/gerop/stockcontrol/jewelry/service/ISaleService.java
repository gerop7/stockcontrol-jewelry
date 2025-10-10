package com.gerop.stockcontrol.jewelry.service;

import java.util.List;

import com.gerop.stockcontrol.jewelry.model.dto.sale.CompleteSaleDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.SaleResultDto;
import com.gerop.stockcontrol.jewelry.model.entity.Sale;

public interface ISaleService {
    SaleResultDto processSale(CompleteSaleDto sale);
    List<Sale> listAll();
    List<Sale> listAllOrderByTotalAsc();
    List<Sale> listAllOrderByTotalDesc();
}
