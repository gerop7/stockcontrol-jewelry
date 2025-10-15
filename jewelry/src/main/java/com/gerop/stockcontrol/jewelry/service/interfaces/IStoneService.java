package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.Stone;

public interface IStoneService {

    public List<Stone> findAllById(List<Long> stoneIds);

}
