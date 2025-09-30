package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Composition;

@Repository
public interface CompositionRepository extends CrudRepository<Composition, Long>{
    List<Composition> findAllById(List<Long> ids);
}
