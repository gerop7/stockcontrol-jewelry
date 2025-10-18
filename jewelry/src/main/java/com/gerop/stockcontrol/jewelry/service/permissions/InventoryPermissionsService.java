package com.gerop.stockcontrol.jewelry.service.permissions;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.InventoryUserPermissions;
import com.gerop.stockcontrol.jewelry.model.entity.User;
import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryPermissionsStatus;
import com.gerop.stockcontrol.jewelry.model.entity.enums.InventoryUserPermissionType;
import com.gerop.stockcontrol.jewelry.repository.InventoryRepository;
import com.gerop.stockcontrol.jewelry.repository.InventoryUserPermissionsRepository;
import com.gerop.stockcontrol.jewelry.repository.UserRepository;
import com.gerop.stockcontrol.jewelry.service.UserServiceHelper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class InventoryPermissionsService implements IInventoryPermissionsService{
    private final InventoryRepository inventoryRepository;
    private final InventoryUserPermissionsRepository permissionsRepository;
    private final UserServiceHelper helper;
    private final UserRepository userRepository;

    public InventoryPermissionsService(UserServiceHelper helper, InventoryRepository inventoryRepository, InventoryUserPermissionsRepository permissionsRepository, UserRepository userRepository) {
        this.helper = helper;
        this.inventoryRepository = inventoryRepository;
        this.permissionsRepository = permissionsRepository;
        this.userRepository = userRepository;
    }

    

    @Override
    public boolean isOwner(Long inventoryId, Long userId) {
        return inventoryRepository.existsByIdAndOwnerId(inventoryId, userId);
    }

    @Override
    public boolean canRead(Long inventoryId, Long userId) {
        return isOwner(inventoryId, userId) || permissionsRepository.existsByInventoryIdAndUserIdAndTypeAndStatus(inventoryId, userId, InventoryUserPermissionType.READ, InventoryPermissionsStatus.ACCEPTED)
        || permissionsRepository.existsByInventoryIdAndUserIdAndTypeAndStatus(inventoryId, userId, InventoryUserPermissionType.WRITE, InventoryPermissionsStatus.ACCEPTED);
    }

    @Override
    public boolean canWrite(Long inventoryId, Long userId) {
        return isOwner(inventoryId, userId) || permissionsRepository.existsByInventoryIdAndUserIdAndTypeAndStatus(inventoryId, userId, InventoryUserPermissionType.WRITE, InventoryPermissionsStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public boolean inviteUser(Long inventoryId, Long targetUserId, InventoryUserPermissionType type) {
        Long currentUserId = helper.getCurrentUser().getId();

        if(targetUserId.equals(currentUserId))
            throw new IllegalArgumentException("No puedes invitarte a ti mismo a tu propio inventario");

        if(!isOwner(inventoryId, currentUserId))
            throw new SecurityException("Solo el creador del inventario puede invitar usuarios");
        

        User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Inventory targetInventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado"));

        /*
        Si existe una invitacion lanza exception si esta aceptada o pendiente, 
        y si fue rechazada se vuelve a setear como pendiente, 
        en caso de haber ser de tipo write, se setean la invitacion de leer y de escribir
        */
        Optional<InventoryUserPermissions> permissionOpt = permissionsRepository.findByInventoryIdAndUserIdAndType(inventoryId, targetUserId, type);
        if(permissionOpt.isPresent()){
            InventoryUserPermissions permission = permissionOpt.get();
            if(permission.getStatus() == InventoryPermissionsStatus.PENDING || permission.getStatus() == InventoryPermissionsStatus.ACCEPTED)
                throw new IllegalStateException("Ya existe una invitacion pendiente o aceptada para este usuario");
            else{
                permission.setStatus(InventoryPermissionsStatus.PENDING);
                permissionsRepository.save(permission);

                if(type==InventoryUserPermissionType.WRITE){
                    permissionsRepository.findByInventoryIdAndUserIdAndType(inventoryId, targetUserId, InventoryUserPermissionType.READ)
                    .ifPresent(readPerm -> {
                        readPerm.setStatus(InventoryPermissionsStatus.PENDING);
                        permissionsRepository.save(readPerm);
                    });
                }
                
                return true;
            }
        }

        
        InventoryUserPermissions newPermission = new InventoryUserPermissions(targetInventory, InventoryPermissionsStatus.PENDING, type, targetUser);
        permissionsRepository.save(newPermission);

        if(type== InventoryUserPermissionType.WRITE){
            permissionsRepository.findByInventoryIdAndUserIdAndType(inventoryId, targetUserId, InventoryUserPermissionType.READ)
            .ifPresentOrElse(readPerm -> {
                readPerm.setStatus(InventoryPermissionsStatus.PENDING);
                permissionsRepository.save(readPerm);
            },
            () -> {
                InventoryUserPermissions newPermissionRead = new InventoryUserPermissions(targetInventory, InventoryPermissionsStatus.PENDING, InventoryUserPermissionType.READ, targetUser);
                permissionsRepository.save(newPermissionRead);
            });

        }
        return true;
    }

    @Override
    public boolean respondToInvitation(Long inventoryId, boolean accept) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteInvitation(Long inventoryId, Long targetUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<InventoryUserPermissions> listPendingInvitations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
