package com.gerop.stockcontrol.jewelry.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;

public interface ISubcategoryService {

    public Optional<Subcategory> findOne(Long subcategoryId);

    public void addToInventories(Subcategory sub, List<Inventory> inventories);

    public Optional<Subcategory> findOneWithOwner(Long subId);

    Subcategory save(Subcategory sub);

}
