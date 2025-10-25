package com.gerop.stockcontrol.jewelry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingJewelRestock;

@Repository
public interface PendingJewelRestockRepository extends JpaRepository<PendingJewelRestock, Long> {

    Optional<PendingJewelRestock> findByJewelIdAndInventoryId(Long jewelId, Long inventoryId);

    boolean existsByJewelIdAndInventoryId(Long jewelId, Long inventoryId);

}
