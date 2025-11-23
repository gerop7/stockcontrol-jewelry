package com.gerop.stockcontrol.jewelry.repository;

import com.gerop.stockcontrol.jewelry.model.entity.AbstractCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BaseCategoryRepository<C extends AbstractCategory> extends CrudRepository<C, Long> {
    Optional<C> findByIdWithOwner(@Param("subId") Long catId);

    List<C> findAllByUser(Long userId);

    List<C> findAllByInventory(Long inventoryId);

    List<C> findAllByUserNotInInventory(Long id, Long inventoryId);
}
