package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Sale;
import com.gerop.stockcontrol.jewelry.model.entity.User;

@Repository
public interface SaleRepository extends CrudRepository<Sale, Long> {
    List<Sale> findAllByUserOrderByTimestampDesc(User user);
    List<Sale> findAllByUserOrderByTotalAsc(User user);
    List<Sale> findAllByUserOrderByTotalDesc(User user);
}
