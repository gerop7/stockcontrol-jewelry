package com.gerop.stockcontrol.jewelry.model.dto.sale;

import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.Sale;

public class SaleResultDto {
    private Sale sale;
    private List<String> fails;
    public SaleResultDto(Sale sale, List<String> fails) {
        this.sale = sale;
        this.fails = fails;
    }

    public SaleResultDto(List<String> fails, Sale sale) {
        this.fails = fails;
        this.sale = sale;
    }
    public Sale getSale() {
        return sale;
    }
    public void setSale(Sale sale) {
        this.sale = sale;
    }
    public List<String> getFails() {
        return fails;
    }
    public void setFails(List<String> fails) {
        this.fails = fails;
    }


}
