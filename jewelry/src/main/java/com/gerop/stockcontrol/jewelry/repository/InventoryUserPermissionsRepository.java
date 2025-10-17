package com.gerop.stockcontrol.jewelry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.InventoryUserPermissions;
import com.gerop.stockcontrol.jewelry.model.entity.enums.UserInventoryPermissionType;

@Repository
public interface InventoryUserPermissionsRepository extends JpaRepository<InventoryUserPermissions, Object> {

    boolean existsByInventoryIdAndUserIdAndType(Long inventoryId, Long userId, UserInventoryPermissionType read);

}
