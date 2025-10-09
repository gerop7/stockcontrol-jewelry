package com.gerop.stockcontrol.jewelry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.model.dto.CompleteSaleDto;
import com.gerop.stockcontrol.jewelry.model.dto.JewelSaleWithPendingRestockDto;
import com.gerop.stockcontrol.jewelry.model.dto.MetalWeightDto;
import com.gerop.stockcontrol.jewelry.model.dto.StoneQuantityDto;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Sale;
import com.gerop.stockcontrol.jewelry.repository.SaleRepository;

@Service
public class SaleService implements ISaleService {
    private final SaleRepository repository;
    private final IJewelService jewelService;
    private final IMetalService metalService;
    private final IStoneService stoneService;
    private final UserServiceHelper helper;

    public SaleService(UserServiceHelper helper, IJewelService jewelService, IMetalService metalService, SaleRepository repository, IStoneService stoneService) {
        this.helper = helper;
        this.jewelService = jewelService;
        this.metalService = metalService;
        this.repository = repository;
        this.stoneService = stoneService;
    }

    @Override
    @Transactional
    public Optional<Sale> processSale(CompleteSaleDto saleDto) {
        Sale sale = new Sale();
        sale.setDescription(saleDto.getDescription());
        sale.setTotal(saleDto.getTotal());
        sale.setUser(helper.getCurrentUser());
        
        for(JewelSaleWithPendingRestockDto jewelData: saleDto.getJewels()){
            Jewel j = jewelService.findById(jewelData.getJewelId()).orElseThrow();
            jewelService.sale(jewelData.getQuantity(), jewelData.getJewelId(), jewelData.getQuantityToRestock());
            for(MetalWeightDto metalToRestock : jewelData.getMetalToRestock()){
                metalService.pendingRestock(metalToRestock.getMetalId(),metalToRestock.getWeight());
            }
            for(StoneQuantityDto stoneToRestock: jewelData.getStoneToRestock()){
                stoneService.pendingRestock(stoneToRestock.getStoneId(),stoneToRestock.getQuantity());
            }

        }
    }

    @Override
    @Transactional(readOnly= true)
    public List<Sale> listAll() {
        return repository.findAllByUserOrderByTimestampDesc(helper.getCurrentUser());
    }

    @Override
    @Transactional(readOnly= true)
    public List<Sale> listAllOrderByTotalAsc() {
        return repository.findAllByUserOrderByTotalAsc(helper.getCurrentUser());
    }

    @Override
    @Transactional(readOnly= true)
    public List<Sale> listAllOrderByTotalDesc() {
        return repository.findAllByUserOrderByTotalDesc(helper.getCurrentUser());
    }

}
