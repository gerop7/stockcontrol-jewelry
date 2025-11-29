package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;
import java.util.Optional;

import com.gerop.stockcontrol.jewelry.model.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Subcategory;

@Repository
public interface SubcategoryRepository extends BaseCategoryRepository<Subcategory>{
    @Query("""
        SELECT DISTINCT s FROM Subcategory s
        LEFT JOIN FETCH s.owner
        JOIN FETCH c.inventories
        JOIN FETCH s.principalCategory
        WHERE s.id = :subId
    """)
    Optional<Subcategory> findByIdWithOwner(@Param("subId") Long subId);

    @Query("""
        SELECT DISTINCT c FROM Subcategory c
        JOIN c.owner o
        JOIN FETCH c.principalCategory
        WHERE o.id = :userId OR c.global = true
    """)
    @Override
    List<Subcategory> findAllByUser(Long userId);

    @Override
    @Query("""
        SELECT DISTINCT c FROM Subcategory c
        JOIN c.inventories i
        JOIN FETCH c.principalCategory
        WHERE i.id = :inventoryId
    """)
    List<Subcategory> findAllByInventory(Long inventoryId);

    @Query("""
        SELECT DISTINCT c FROM Subcategory c
        JOIN c.owner o
        JOIN FETCH c.principalCategory
        WHERE o.id = :ownerId
            AND c.id NOT IN (
                  SELECT c2.id FROM Subcategory c2
                  JOIN c2.inventories inv
                  WHERE inv.id = :inventoryId
            )
    """)
    @Override
    List<Subcategory> findAllByUserNotInInventory(Long id, Long inventoryId);

    @Query("""
        SELECT DISTINCT c FROM Subcategory c
        JOIN c.inventories i
        JOIN c.principalCategory pc
        LEFT JOIN FETCH c.owner o
        WHERE i.id = :inventoryId AND pc.id = :principalCategoryId
    """)
    @Override
    List<Subcategory> findAllByPrincipalCategoryAndInventory(Long principalCategoryId, Long inventoryId);

    @Override
    @Query("""
        SELECT DISTINCT c FROM Subcategory c
        JOIN c.principalCategory pc
        LEFT JOIN FETCH c.owner o
        WHERE pc.id = :principalCategoryId
    """)
    List<Subcategory> findAllByPrincipalCategory(Long principalCategoryId);
}
