package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.User;
import com.gerop.stockcontrol.jewelry.model.entity.enums.JewelMovementType;
import com.gerop.stockcontrol.jewelry.model.entity.movement.JewelMovement;

@Repository
public interface JewelMovementRepository extends JpaRepository<JewelMovement, Long>{
    List<JewelMovement> findAllByUserOrderByTimestampDesc(User user);
    List<JewelMovement> findAllByUserAndTypeOrderByTimestampDesc(User user,JewelMovementType type);
}
