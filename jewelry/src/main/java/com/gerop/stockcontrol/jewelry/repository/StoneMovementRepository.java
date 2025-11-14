package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.enums.CompositionMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.StoneMovement;

@Repository
public interface StoneMovementRepository extends CrudRepository<StoneMovement, Long>{
    List<StoneMovement> findAllByUserIdOrderByTimestampDesc(Long id);
    List<StoneMovement> findAllByUserIdAndTypeOrderByTimestampDesc(Long id, CompositionMovementType type);
}
