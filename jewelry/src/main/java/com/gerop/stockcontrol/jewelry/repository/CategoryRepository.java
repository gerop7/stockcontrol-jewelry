package com.gerop.stockcontrol.jewelry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Category;


@Repository
public interface CategoryRepository extends BaseCategoryRepository<Category>{
    @Query("""
        SELECT DISTINCT c FROM Category c
        LEFT JOIN FETCH c.owner
        LEFT JOIN FETCH c.inventories
        WHERE c.id = :catId
    """)
    Optional<Category> findByIdWithOwner(@Param("catId") Long catId);
}
