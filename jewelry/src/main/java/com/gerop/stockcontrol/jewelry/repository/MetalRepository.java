package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.Metal;

@Repository
public interface MetalRepository extends MaterialBaseRepository<Metal> {

    /*
     * Trae metal + stockByInventory
     * NO pending, NO user
     */
    @Query("""
        SELECT DISTINCT m FROM Metal m
        LEFT JOIN FETCH m.stockByInventory st
        LEFT JOIN FETCH st.inventory
        WHERE m.id = :id
    """)
    @Override
    Optional<Metal> findByIdWithStockByInventory(@Param("id") Long metalId);



    /*
     * Trae TODA la data de un metal por ID:
     * - user
     * - stockByInventory
     * - inventory
     * - pendingMetalRestock
     */
    @Query("""
        SELECT DISTINCT m FROM Metal m
        LEFT JOIN FETCH m.user
        LEFT JOIN FETCH m.stockByInventory st
        LEFT JOIN FETCH st.inventory
        LEFT JOIN FETCH m.pendingMetalRestock p
        LEFT JOIN FETCH p.inventory
        WHERE m.id = :id
    """)
    @Override
    Optional<Metal> findByIdFullData(@Param("id") Long id);



    /*
     * Trae metal por ID + SOLO stock/pending del listado de inventarios indicado.
     * Si no hay stock/pending para esos inventarios → viene vacío pero NO excluye el metal.
     */
    @Query("""
        SELECT DISTINCT m FROM Metal m
        LEFT JOIN FETCH m.user u
    
        LEFT JOIN FETCH m.stockByInventory st
        LEFT JOIN FETCH st.inventory sti
    
        LEFT JOIN FETCH m.pendingMetalRestock p
        LEFT JOIN FETCH p.inventory pi
    
        WHERE m.id = :materialId
        AND (st.inventory.id IN :inventoriesIds OR st.inventory IS NULL)
        AND (p.inventory.id IN :inventoriesIds OR p.inventory IS NULL)
    """)
    @Override
    Optional<Metal> findByIdAndInventoriesIdsFullData(Long materialId, Set<Long> inventoriesIds);



    /*
     * Lista mínima de metales que pertenecen a un inventario
     */
    @Query("""
        SELECT DISTINCT m FROM Metal m
        JOIN m.stockByInventory st
        WHERE st.inventory.id = :inventoryId
    """)
    @Override
    List<Metal> findAllByInventoryId(Long inventoryId);


    @Override
        @Query("""
        SELECT DISTINCT m FROM Metal m
        LEFT JOIN FETCH m.user u
        LEFT JOIN FETCH m.stockByInventory st
        LEFT JOIN FETCH st.inventory sti
        LEFT JOIN FETCH m.pendingMetalRestock p
        LEFT JOIN FETCH p.inventory pi
        WHERE (st.inventory.id = :inventoryId OR st.inventory IS NULL)
          AND (p.inventory.id = :inventoryId OR p.inventory IS NULL)
    """)
    List<Metal> findAllByInventoryFullData(@Param("inventoryId") Long inventoryId);


    /*
     * Lista de metales visibles por el usuario:
     * - Los del usuario
     * - Los globales
     */
    @Query("""
        SELECT m FROM Metal m
        WHERE m.user.id = :ownerId
           OR m.isGlobal = true
    """)
    @Override
    List<Metal> findAllByOwnerId(Long ownerId);



    /*
     * Lista completa de metales visibles por usuario
     * y con stock/pending SOLO de los inventarios permitidos
     */
    @Query("""
        SELECT DISTINCT m FROM Metal m
        LEFT JOIN FETCH m.user u
    
        LEFT JOIN FETCH m.stockByInventory st
        LEFT JOIN FETCH st.inventory sti
    
        LEFT JOIN FETCH m.pendingMetalRestock p
        LEFT JOIN FETCH p.inventory pi
    
        WHERE (m.user.id = :ownerId OR m.isGlobal = true)
        AND (st.inventory.id IN :inventoriesIds OR st.inventory IS NULL)
        AND (p.inventory.id IN :inventoriesIds OR p.inventory IS NULL)
    """)
    @Override
    List<Metal> findAllByOwnerIdFullData(Long ownerId, Set<Long> inventoriesIds);



    /*
     * Metales del usuario NO presentes en un inventario (para agregarlos)
     * No incluye globales.
     */
    @Query("""
        SELECT m FROM Metal m
        WHERE m.user.id = :ownerId AND m.isGlobal = false
          AND m.id NOT IN (
                SELECT st.metal.id FROM MetalStockByInventory st
                WHERE st.inventory.id = :inventoryId
          )
    """)
    @Override
    List<Metal> findAllByOwnerIdNotInInventory(Long ownerId, Long inventoryId);

}
