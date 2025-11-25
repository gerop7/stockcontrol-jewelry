package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;
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
        JOIN FETCH c.inventories
        WHERE c.id = :catId
    """)
    Optional<Category> findByIdWithOwner(@Param("catId") Long catId);

    @Query("""
        SELECT DISTINCT c FROM Category c
        LEFT JOIN FETCH c.owner o
        WHERE o.id = :userId OR c.global = true
    """)
    @Override
    List<Category> findAllByUser(Long userId);

    @Query("""
        SELECT DISTINCT c FROM Category c
        JOIN c.inventories i
        LEFT JOIN FETCH c.owner o
        WHERE i.id = :inventoryId
    """)
    @Override
    List<Category> findAllByInventory(Long inventoryId);

    @Query("""
        SELECT DISTINCT c FROM Category c
        LEFT JOIN FETCH c.owner o
        WHERE o.id = :ownerId
            AND c.id NOT IN (
                  SELECT c2.id FROM Category c2
                  JOIN c2.inventories inv
                  WHERE inv.id = :inventoryId
            )
    """)
    @Override
    List<Category> findAllByUserNotInInventory(Long ownerId, Long inventoryId);
}
