package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.entity.Category;

public interface ICategoryService {

    Optional<Category> findOne(Long catId);
    
}
