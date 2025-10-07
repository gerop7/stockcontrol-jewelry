package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.dto.CompleteSaleDto;
import com.gerop.stockcontrol.jewelry.model.entity.Sale;
import com.gerop.stockcontrol.jewelry.repository.SaleRepository;

@Service
public class SaleService implements ISaleService {
    private final SaleRepository repository;
    private final UserServiceHelper helper;

    public SaleService(UserServiceHelper helper, SaleRepository repository) {
        this.helper = helper;
        this.repository = repository;
    }

    @Override
    public Optional<Sale> processSale(CompleteSaleDto sale) {
        return Optional.empty();
    }

    @Override
    public List<Sale> listAll() {
        return repository.findAllByUserOrderByTimestampDesc(helper.getCurrentUser());
    }

    @Override
    public List<Sale> listAllOrderByTotalAsc() {
        return repository.findAllByUserOrderByTotalAsc(helper.getCurrentUser());
    }

    @Override
    public List<Sale> listAllOrderByTotalDesc() {
        return repository.findAllByUserOrderByTotalDesc(helper.getCurrentUser());
    }

}
