package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.MetalMovement;

@Repository
public interface MetalMovementRepository extends JpaRepository<MetalMovement, Long> {
    List<MetalMovement> findAllByOrderByTimestampDesc();
    List<MetalMovement> findAllByTypeOrderByTimestampDesc(CompositionMovementType type);
}
