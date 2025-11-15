package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Stone;

@Repository
public interface StoneRepository extends CrudRepository<Stone, Long>{
    List<Stone> findAllByIdAndUserId(List<Long> ids, Long stoneId);

    public boolean existsByIdAndUserId(Long materialId, Long userId);

    @Query("""
        SELECT DISTINCT s FROM Stone s
        LEFT JOIN FETCH s.stockByInventory st
        LEFT JOIN FETCH st.inventory
        WHERE s.id = :id
    """)
    Optional<Stone> findByIdWithStockByInventory(Long stoneId);
}
