package com.gerop.stockcontrol.jewelry.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;

@Repository
public interface SubcategoryRepository extends CrudRepository<Subcategory, Long>{
    Optional<Subcategory> findByIdAndUserId(Long id, Long userId);
}
