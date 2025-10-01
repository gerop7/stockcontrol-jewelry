package com.gerop.stockcontrol.jewelry.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Movement;

@Repository
public interface MovementRepository extends CrudRepository<Movement, Long>{

}
