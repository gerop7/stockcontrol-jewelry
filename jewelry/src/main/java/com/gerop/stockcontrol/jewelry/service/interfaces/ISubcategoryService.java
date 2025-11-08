package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;

public interface ISubcategoryService {

    public Optional<Subcategory> findOne(Long subcategoryId);

}
