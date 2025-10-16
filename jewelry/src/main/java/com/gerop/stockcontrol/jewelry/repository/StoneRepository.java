package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Stone;

@Repository
public interface StoneRepository extends CrudRepository<Stone, Long>{
    List<Stone> findAllByIdAndUserId(List<Long> ids, Long stoneId);
}
