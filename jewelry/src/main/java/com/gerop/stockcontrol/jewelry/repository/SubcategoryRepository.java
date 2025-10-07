package com.gerop.stockcontrol.jewelry.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;

@Repository
public interface SubcategoryRepository extends CrudRepository<Subcategory, Long>{
}
