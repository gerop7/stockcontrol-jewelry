package com.gerop.stockcontrol.jewelry.mapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gerop.stockcontrol.jewelry.model.dto.sale.JewelSummaryDto;
import com.gerop.stockcontrol.jewelry.model.dto.sale.SaleListDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.Sale;
import com.gerop.stockcontrol.jewelry.model.entity.SaleJewel;

@Component
public class SaleMapper {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SaleListDto toListDto(Sale sale) {
        Inventory inventory = sale.getInventory();
        String inventoryName = inventory != null?inventory.getName():"Sin inventario";
        Long inventoryId = inventory != null?inventory.getId():null;
        Float saleTotal = 0.0f;

        List<JewelSummaryDto> jewelDtos = new ArrayList<>();

        for (SaleJewel saleJewel:sale.getJewels()) {
            Jewel jewel = saleJewel.getJewel();
            Long quantity = saleJewel.getQuantity();
            Float total = saleJewel.getTotal() * quantity;

            saleTotal += total;

            jewelDtos.add(new JewelSummaryDto(
                jewel.getId(),
                jewel.getName(),
                jewel.getSku(),
                inventoryId,
                inventoryName,
                quantity,
                total
            ));
        }

        return new SaleListDto(
            sale.getId(),
            sale.getDescription(),
            saleTotal,
            sale.getTimestamp() != null ? sale.getTimestamp().format(FORMATTER) : "",
            inventoryName,
            inventoryId,
            jewelDtos
        );
    }
}