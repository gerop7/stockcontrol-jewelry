package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.dto.CompleteSaleDto;
import com.gerop.stockcontrol.jewelry.model.entity.Sale;

public interface ISaleService {
    Optional<Sale> processSale(CompleteSaleDto sale);
    List<Sale> listAll();
    List<Sale> listAllOrderByTotalAsc();
    List<Sale> listAllOrderByTotalDesc();
}
