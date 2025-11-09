package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.entity.Category;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;

public interface ICategoryService {

    Optional<Category> findOne(Long catId);

    public void addToInventories(Category cat, List<Inventory> inventories);

    Optional<Category> findOneWithOwner(Long catId);

    Category save(Category cat);
    
}
