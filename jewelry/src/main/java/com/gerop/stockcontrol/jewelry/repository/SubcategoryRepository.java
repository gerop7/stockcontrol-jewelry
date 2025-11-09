package com.gerop.stockcontrol.jewelry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;

@Repository
public interface SubcategoryRepository extends CrudRepository<Subcategory, Long>{
    Optional<Subcategory> findByIdAndUserId(Long id, Long userId);

    @Query("""
        SELECT s FROM Subcategory s
        LEFT JOIN FETCH s.owner
        LEFT JOIN FETCH s.principalCategory
        WHERE s.id = :subId
    """)
    Optional<Subcategory> findByIdWithOwner(@Param("subId") Long subId);
}
