package com.gerop.stockcontrol.jewelry.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.StockNotAvailableException;
import com.gerop.stockcontrol.jewelry.mapper.SaleMapper;
import com.gerop.stockcontrol.jewelry.model.dto.MetalWeightDto;
import com.gerop.stockcontrol.jewelry.model.dto.StoneQuantityDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.CompleteSaleDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.JewelSaleWithPendingRestockDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.SaleListDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.SaleResultDto;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Sale;
import com.gerop.stockcontrol.jewelry.model.entity.SaleJewel;
import com.gerop.stockcontrol.jewelry.repository.SaleRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IJewelService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMetalService;
import com.gerop.stockcontrol.jewelry.service.interfaces.ISaleService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IStoneService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SaleService implements ISaleService {
    private final SaleRepository repository;
    private final IJewelService jewelService;
    private final IMetalService metalService;
    private final IStoneService stoneService;
    private final UserServiceHelper helper;
    private final SaleMapper mapper;

    public SaleService(SaleRepository repository, IJewelService jewelService, IMetalService metalService,
            IStoneService stoneService, UserServiceHelper helper, SaleMapper mapper) {
        this.repository = repository;
        this.jewelService = jewelService;
        this.metalService = metalService;
        this.stoneService = stoneService;
        this.helper = helper;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public SaleResultDto processSale(CompleteSaleDto saleDto) {
        
    }

    @Override
    @Transactional(readOnly= true)
    public List<SaleListDto> listAll() {
        return repository.findAllByUserIdOrderByTimestampDesc(helper.getCurrentUser().getId());
    }

    @Override
    @Transactional(readOnly= true)
    public List<SaleListDto> listAllOrderByTotalAsc() {
        return repository.findAllByUserIdOrderByTotalAsc(helper.getCurrentUser().getId());
    }

    @Override
    @Transactional(readOnly= true)
    public List<SaleListDto> listAllOrderByTotalDesc() {
        return repository.findAllByUserIdOrderByTotalDesc(helper.getCurrentUser().getId());
    }

}
