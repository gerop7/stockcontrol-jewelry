package com.gerop.stockcontrol.jewelry.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingRestock;

@Repository
public interface PendingRestockRepository extends CrudRepository<PendingRestock, Long> {

}
