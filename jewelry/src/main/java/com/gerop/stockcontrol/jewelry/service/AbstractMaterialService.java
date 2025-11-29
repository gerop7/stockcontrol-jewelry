package com.gerop.stockcontrol.jewelry.service;

import java.text.Normalizer;
import java.util.*;

import com.gerop.stockcontrol.jewelry.exception.InvalidQuantityException;
import com.gerop.stockcontrol.jewelry.exception.RequiredFieldException;
import com.gerop.stockcontrol.jewelry.exception.inventory.InventoryNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.material.MaterialNotFoundException;
import com.gerop.stockcontrol.jewelry.exception.material.MaterialPermissionDeniedException;
import com.gerop.stockcontrol.jewelry.mapper.MaterialMapper;
import com.gerop.stockcontrol.jewelry.model.dto.materials.MaterialDto;
import com.gerop.stockcontrol.jewelry.model.entity.Inventory;
import com.gerop.stockcontrol.jewelry.model.entity.Material;
import java.util.function.Function;
import com.gerop.stockcontrol.jewelry.model.entity.User;
import com.gerop.stockcontrol.jewelry.model.entity.movement.Movement;
import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingRestock;
import com.gerop.stockcontrol.jewelry.model.entity.stockbyinventory.StockByInventory;
import com.gerop.stockcontrol.jewelry.repository.MaterialBaseRepository;
import com.gerop.stockcontrol.jewelry.service.interfaces.IInventoryService;
import com.gerop.stockcontrol.jewelry.service.interfaces.IMaterialService;
import com.gerop.stockcontrol.jewelry.service.movement.IMaterialMovementService;
import com.gerop.stockcontrol.jewelry.service.pendingtorestock.IPendingRestockService;
import com.gerop.stockcontrol.jewelry.service.permissions.IMaterialPermissionsService;
import com.gerop.stockcontrol.jewelry.service.stockperinventory.IStockByInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class AbstractMaterialService<M extends Material, N extends Number,
        MDto extends MaterialDto, R extends MaterialBaseRepository<M>, Mov extends Movement, Pend extends PendingRestock, S extends StockByInventory<N>> implements IMaterialService<M, N, MDto> {

    protected final R repository;
    protected final UserServiceHelper helperService;
    protected final IMaterialPermissionsService<M> permissionsService;
    protected final IMaterialMovementService<Mov, M, N> movementService;
    protected final IPendingRestockService<Pend, N, M> pendingRestockService;
    protected final IInventoryService inventoryService;
    protected final MaterialMapper mapper;
    protected final IStockByInventoryService<S, M, N> stockService;
    // Factory para crear la entidad específica (Metal, Stone…)
    protected final Function<MDto, M> materialFactory;

    protected void validateOwner(M material) {
        if (!permissionsService.isOwner(material.getId(), helperService.getCurrentUser().getId()))
            throw new MaterialPermissionDeniedException("No tienes permiso para modificar este material.");
    }

    protected void validateQuantity(N quantity) {
        if (quantity == null || quantity.doubleValue() < 0)
            throw new InvalidQuantityException("La cantidad no puede ser negativa.");
    }

    protected void validateUpdateStock(M material, Inventory inventory) {
        if (!permissionsService.canUpdateStock(material.getId(), helperService.getCurrentUser().getId(), inventory.getId()))
            throw new MaterialPermissionDeniedException("No tienes permisos para modificar stock en este inventario.");
    }

    protected void validateCanCreate(Long inventoryId){
        if(!permissionsService.canCreate(inventoryId)){
            throw new MaterialPermissionDeniedException("No tienes permisos para crear "+getClass()+" en este inventario.");
        }
    }

    protected Inventory getInventoryOrThrow(Long id){
        return inventoryService.findOne(id)
                .orElseThrow(()-> new InventoryNotFoundException(id));
    }


    //METODOS ABSTRACTOS
    protected abstract void createInitialInventoryAndRestock(M material, MDto dto);
    protected abstract String className();
    protected abstract void addToInventoryInternal(M material, Inventory inventory, N quantity);
    protected abstract void removeFromInventoryInternal(M material, Inventory inventory);

    @Override
    @Transactional
    public MDto create(MDto dto) {

        User user = helperService.getCurrentUser();
        M material = materialFactory.apply(dto);

        material.setUser(user);
        material = save(material);

        createInitialInventoryAndRestock(material, dto);

        return (MDto) mapper.toDto(save(material));
    }


    @Override
    @Transactional
    public boolean delete(Long materialId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public boolean delete(M material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public M save(M material) {
        return repository.save(material);
    }

    @Override
    @Transactional
    public void update(MDto data) {
        if(data==null)
            throw new RequiredFieldException("No hay informacion para actualizar!");
        M material = repository.findById(data.id())
                .orElseThrow(() -> new MaterialNotFoundException(data.id(), className()));
        validateOwner(material);

        if (data.url() != null) material.setUrlImage(data.url());
        if (data.name() != null) material.setName(data.name());

        save(material);
    }

    @Override
    @Transactional
    public void addStock(Long materialId, Long inventoryId, N quantity, String description) {
        Inventory inventory = inventoryService.findOne(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
        M material = findOne(materialId)
                .orElseThrow(() -> new MaterialNotFoundException(materialId,className()));

        addStock(material, inventory, quantity, description);
    }

    @Override
    @Transactional
    public void addStock(M material, Inventory inventory, N quantity, String description) {
        validateUpdateStock(material, inventory);
        validateQuantity(quantity);

        stockService.addStock(material,inventory, quantity);
        pendingRestockService.removeFromRestock(material, inventory, quantity);
        movementService.inflow(quantity, material, inventory);

        save(material);
    }

    @Transactional
    protected void outflow(M material, Inventory inventory, N quantity, N quantityToRestock){
        validateUpdateStock(material, inventory);

        stockService.removeStock(material, inventory, quantity);
        pendingRestockService.removeFromRestock(material,inventory,quantityToRestock);
    }

    @Override
    @Transactional
    public void outflowByWork(Long materialId, Long inventoryId, N quantity, N quantityToRestock) {
        Inventory inventory = inventoryService.findOne(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
        M material = findOne(materialId)
                .orElseThrow(() -> new MaterialNotFoundException(materialId,className()));

        outflowByWork(material, inventory, quantity, quantityToRestock);
    }

    @Override
    @Transactional
    public void outflowByWork(M material, Inventory inventory, N quantity, N quantityToRestock) {
        outflow(material, inventory, quantity, quantityToRestock);
        movementService.outflow(quantity, material, inventory);

        save(material);
    }

    @Override
    @Transactional
    public MDto sale(Long materialId, Long inventoryId, N quantity, Float total, N quantityToRestock) {
        Inventory inventory = inventoryService.findOne(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
        M material = findOne(materialId)
                .orElseThrow(() -> new MaterialNotFoundException(materialId,className()));

        outflow(material, inventory, quantity,quantityToRestock);
        movementService.sale(material,inventory,quantity,total);

        return (MDto) mapper.toDto(save(material));
    }

    @Override
    @Transactional
    public void addToInventory(Long materialId, Inventory inventory, N quantity) {
        if(!stockService.existByIdAndInventoryId(materialId,inventory.getId())){
            M material = repository.findByIdWithStockByInventory(materialId)
                    .orElseThrow(()-> new MaterialNotFoundException(materialId,className()));

            if(!permissionsService.canAddToInventory(materialId, helperService.getCurrentUser().getId(), inventory.getId()))
                throw new MaterialPermissionDeniedException("No puedes añadir el "+className()+" "+material.getName()+" en el Inventario "+inventory.getName()+".");

            if(!material.isGlobal()){
                addToInventoryInternal(material, inventory, quantity);
            }

            save(material);
        }
    }

    @Transactional
    @Override
    public void addToInventory(M mat, Inventory inventory, N quantity) {
        if(!stockService.existByIdAndInventoryId(mat.getId(),inventory.getId())){
            if(!mat.isGlobal()){
                addToInventoryInternal(mat, inventory, quantity);
            }
            save(mat);
        }
    }

    @Override
    @Transactional
    public void removeFromInventory(Long materialId, Inventory inventory) {
        M material = repository.findByIdWithStockByInventory(materialId)
                .orElseThrow(()-> new MaterialNotFoundException(materialId,className()));

        if(!permissionsService.canDeleteFromInventory(materialId, inventory.getId()))
            throw new MaterialPermissionDeniedException("No puedes eliminar el "+className()+" "+material.getName()+" en el Inventario "+inventory.getName()+".");

        removeFromInventoryInternal(material, inventory);

        pendingRestockService.remove(material, inventory);

        save(material);
    }

    @Override
    @Transactional
    public void addPendingToRestock(Long materialId, N quantity, Long inventoryId) {
        Inventory inventory = inventoryService.findOne(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
        M material = findOne(materialId)
                .orElseThrow(() -> new MaterialNotFoundException(materialId,className()));

        addPendingToRestock(material, quantity, inventory);
    }

    @Override
    @Transactional
    public void addPendingToRestock(M material, N quantity, Inventory inventory) {
        validateUpdateStock(material, inventory);

        pendingRestockService.addToRestock(material, inventory, quantity);
    }

    @Override
    @Transactional
    public void removePendingToRestock(Long materialId, N quantity, Long inventoryId) {
        Inventory inventory = inventoryService.findOne(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
        M material = findOne(materialId)
                .orElseThrow(() -> new MaterialNotFoundException(materialId,className()));

        removePendingToRestock(material, quantity, inventory);
    }

    @Override
    @Transactional
    public void removePendingToRestock(M material, N quantity, Inventory inventory) {
        validateUpdateStock(material, inventory);

        pendingRestockService.removeFromRestock(material, inventory, quantity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<M> findOne(Long materialId) {
        return repository.findById(materialId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<M> findOneFullData(Long materialId) {
        return repository.findByIdFullData(materialId);
    }

    /*
     *
     * Muestra un unico material, con la informacion de los inventarios a los que tiene acceso el usuario.
     * trae info basica, stock, usuario, y pendientes de reponer.
     *
     * */
    @Override
    @Transactional(readOnly = true)
    public MDto findOneDto(Long materialId, Set<Long> inventoriesIds){
        return (MDto) mapper.toDto(repository.findByIdAndInventoriesIdsFullData(materialId,inventoriesIds).orElseThrow(()-> new MaterialNotFoundException(materialId,className())));
    }

    /*
     *
     * Muestra un unico material, con la informacion de ese inventario,
     * info basica, stock, usuario, y pendientes de reponer.
     *
     * */
    @Override
    @Transactional(readOnly = true)
    public MDto findOneByIdAndInventoryDto(Long materialId, Long inventoryId){
        return (MDto) mapper.toDto(repository.findByIdAndInventoriesIdsFullData(materialId, Collections.singleton(inventoryId)).orElseThrow(()-> new MaterialNotFoundException(materialId,className())));
    }

    /*
    *
    * Listado de materiales de un inventario con info minima
    *
    * */
    @Override
    @Transactional(readOnly = true)
    public List<MDto> findAllByInventoryDto(Long inventoryId){
        return (List<MDto>) repository.findAllByInventoryId(inventoryId).stream().map(mapper::toDto);
    }

    /*
     *
     * Listado de materiales de la seccion materiales del inventario
     *
     * */
    @Transactional(readOnly = true)
    @Override
    public List<MDto> findAllByInventoryFullDataDto(Long inventoryId){
        return (List<MDto>) repository.findAllByInventoryFullData(inventoryId).stream().map(mapper::toDto);
    }

    /*
     *
     * Listado de materiales del usuario con informacion minima
     *
     * */
    @Override
    @Transactional(readOnly = true)
    public List<MDto> findAllByCurrentUserDto(){
        return (List<MDto>) repository.findAllByOwnerId(helperService.getCurrentUser().getId()).stream().map(mapper::toDto);
    }

    /*
     *
     * Listado de materiales de la seccion materiales del usuario
     *
     * */
    @Transactional(readOnly = true)
    @Override
    public List<MDto> findAllByCurrentUserFullDataDto(Set<Long> inventoriesIds){
        return (List<MDto>) repository.findAllByOwnerIdFullData(helperService.getCurrentUser().getId(), inventoriesIds).stream().map(mapper::toDto);
    }


    /*
     *
     * Listado de materiales que no existen en un inventario con informacion minima.
     * Usado para agregar materiales a un inventario.
     *
     * */
    @Override
    @Transactional(readOnly = true)
    public List<MDto> findAllByUserNotInInventory(Long inventoryId){
        return (List<MDto>) repository.findAllByOwnerIdNotInInventory(helperService.getCurrentUser().getId(), inventoryId).stream().map(mapper::toDto);
    }

    /*
     *
     * Listado de materiales que pueden usarse para crear, modificar stock, etc...
     * Muestra materiales del usuario y los del inventario.
     *
     * */
    @Override
    @Transactional(readOnly = true)
    public List<MDto> findAllToUseInInventory(Long inventoryId){

        List<MDto> allByUser = findAllByUserNotInInventory(inventoryId);
        List<MDto> allByInventory = findAllByInventoryDto(inventoryId);

        Set<MDto> materials = new HashSet<>();

        materials.addAll(allByUser);
        materials.addAll(allByInventory);

        return materials.stream().toList();
    }

    private String normalize(String s) {
        return Normalizer.normalize(s.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    @Override
    @Transactional(readOnly = true)
    public List<MDto> findAllContainsName(String name, List<MDto> materials){
        if(name!=null && !name.isEmpty()){
            String filter = normalize(name);
            return materials.stream().filter(mDto -> normalize(mDto.name()).contains(filter)).toList();
        }else
            return materials;
    }
}
