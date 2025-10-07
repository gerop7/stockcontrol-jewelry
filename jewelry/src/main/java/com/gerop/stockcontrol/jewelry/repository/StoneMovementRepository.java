package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.StoneMovement;

@Repository
public interface StoneMovementRepository extends JpaRepository<StoneMovement, Long>{
    List<StoneMovement> findAllByOrderByTimestampDesc();
    List<StoneMovement> findAllByTypeOrderByTimestampDesc(CompositionMovementType type);
}
