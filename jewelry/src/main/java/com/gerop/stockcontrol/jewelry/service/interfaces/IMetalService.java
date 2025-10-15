package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.Metal;

public interface IMetalService {
    public List<Metal> findAllById(List<Long> metalIds);

}
