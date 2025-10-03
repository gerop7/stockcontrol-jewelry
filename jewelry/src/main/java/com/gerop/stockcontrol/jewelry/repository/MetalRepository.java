package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Metal;

@Repository
public interface MetalRepository extends CrudRepository<Metal, Long>{
    List<Metal> findAllById(List<Long> ids);
}
