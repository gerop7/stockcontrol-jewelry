package com.gerop.stockcontrol.jewelry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.InventoryUserPermissions;
import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryPermissionsStatus;
import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryUserPermissionType;

@Repository
public interface InventoryUserPermissionsRepository extends JpaRepository<InventoryUserPermissions, Long> {

    boolean existsByInventoryIdAndUserIdAndType(Long inventoryId, Long userId, InventoryUserPermissionType type);

    public boolean existsByInventoryIdAndUserIdAndTypeAndStatus(Long inventoryId, Long userId, InventoryUserPermissionType type, InventoryPermissionsStatus status);

    public boolean existsByInventoryIdAndUserId(Long inventoryId, Long userId);

    public boolean existsByInventoryIdAndUserIdAndStatus(Long inventoryId, Long userId, InventoryPermissionsStatus status);

    public Optional<InventoryUserPermissions> findByInventoryIdAndUserId(Long inventoryId, Long targetUser);

    public Optional<InventoryUserPermissions> findByInventoryIdAndUserIdAndType(Long inventoryId, Long targetUser, InventoryUserPermissionType type);

    public boolean existsByInventoryIdAndUserIdAndTypeAndStatus(Long inventoryId, Long targetUser, InventoryPermissionsStatus inventoryPermissionsStatus);

    List<InventoryUserPermissions> findAllByUserId(Long id);

    List<InventoryUserPermissions> findAllByUserIdAndStatus(Long id, InventoryPermissionsStatus status);


}
