package com.gerop.stockcontrol.jewelry.service.permissions;

import java.util.List;

import com.gerop.stockcontrol.jewelry.model.entity.InventoryUserPermissions;
import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryUserPermissionType;

public interface IInventoryPermissionsService {
    boolean isOwner(Long inventoryId, Long userId);
    boolean canRead(Long inventoryId, Long userId);
    boolean canWrite(Long inventoryId, Long userId);
    boolean inviteUser(Long inventoryId, Long targetUser, InventoryUserPermissionType type);
    boolean respondToInvitation(Long inventoryId, boolean accept);
    boolean deleteInvitation(Long inventoryId, Long targetUser);
    public List<InventoryUserPermissions> listPendingInvitations();
}
