package com.gerop.stockcontrol.jewelry.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerop.stockcontrol.jewelry.exception.StockNotAvailableException;
import com.gerop.stockcontrol.jewelry.model.dto.MetalWeightDto;
import com.gerop.stockcontrol.jewelry.model.dto.StoneQuantityDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.CompleteSaleDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.JewelSaleWithPendingRestockDto;
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

    public SaleService(UserServiceHelper helper, IJewelService jewelService, IMetalService metalService, SaleRepository repository, IStoneService stoneService) {
        this.helper = helper;
        this.jewelService = jewelService;
        this.metalService = metalService;
        this.repository = repository;
        this.stoneService = stoneService;
    }

    @Transactional(noRollbackFor = {
        StockNotAvailableException.class, 
        EntityNotFoundException.class 
    })
    @Override
    public SaleResultDto processSale(CompleteSaleDto saleDto) {
        Sale sale = new Sale();
        sale.setDescription(saleDto.getDescription());
        sale.setUser(helper.getCurrentUser());
        

        List<String> failedJewels = new ArrayList<>();
        
        for(JewelSaleWithPendingRestockDto jewelData: saleDto.getJewels()){
            Optional<Jewel> jewel = jewelService.findById(jewelData.getJewelId());
            if((jewel.isEmpty() || !jewel.get().isActive()) && jewel.get().getUser().equals(helper.getCurrentUser())){
                failedJewels.add("La joya ID "+jewelData.getJewelId()+" No puede ser vendida.");
            }else{
                try {
                    jewelService.sale(jewelData.getQuantity(), jewelData.getJewelId(), jewelData.getQuantityToRestock());
                    for(MetalWeightDto metalToRestock : jewelData.getMetalToRestock()){
                        try {
                            metalService.pendingRestock(metalToRestock);
                        } catch (EntityNotFoundException e) {
                            failedJewels.add("Metal ID "+metalToRestock.getMetalId()+" No existe.");
                        }
                    }
                    for(StoneQuantityDto stoneToRestock: jewelData.getStoneToRestock()){
                        try {
                            stoneService.pendingRestock(stoneToRestock);
                        } catch (EntityNotFoundException e) {
                            failedJewels.add("Piedra ID "+stoneToRestock.getStoneId()+ " No existe.");
                        }
                    }
                    SaleJewel saleJewel = new SaleJewel();
                    saleJewel.setJewel(jewel.get());
                    saleJewel.setQuantity(jewelData.getQuantity());
                    saleJewel.setTotal(jewelData.getTotal());
                    sale.getJewels().add(saleJewel);
                } catch (StockNotAvailableException e) {
                    failedJewels.add("La venta de "+ jewel.get().getName() +" fallo por: "+ e.getMessage());
                }
            }
        }

        if(!sale.getJewels().isEmpty())
            repository.save(sale);
        
        SaleResultDto saleResult = new SaleResultDto(sale,failedJewels);

        return saleResult;
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
