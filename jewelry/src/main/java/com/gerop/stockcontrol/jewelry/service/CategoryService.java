package com.gerop.stockcontrol.jewelry.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;
import com.gerop.stockcontrol.jewelry.service.interfaces.ICategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    @Override
    public Optional<Category> findOne(Long catId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
