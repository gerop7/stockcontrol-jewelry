package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Metal;

@Repository
public interface MetalRepository extends CrudRepository<Metal, Long>{
    List<Metal> findAllByIdAndUserId(List<Long> ids, Long userId);

    public boolean existsByIdAndUserId(Long materialId, Long userId);

    public boolean existsByIdAndInventoryId(Long materialId, Long inventoryId);

    @Query("SELECT m.user.id FROM Metal m WHERE m.id = :metalId")
    Optional<Long> findUserIdByMetalId(@Param("metalId") Long metalId);
}
