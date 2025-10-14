package com.gerop.stockcontrol.jewelry.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Category;


@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>{
    Optional<Category> findByIdAndUserId(Long id, Long userId);
}
