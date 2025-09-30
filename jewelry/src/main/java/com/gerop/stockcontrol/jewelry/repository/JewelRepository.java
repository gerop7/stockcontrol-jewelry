package com.gerop.stockcontrol.jewelry.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;

@Repository
public interface JewelRepository extends CrudRepository<Jewel, Long> {

    boolean existByName(String name);

    Optional<Jewel> findByName(String name);
}
