package com.yordanov.warehouse.StockService;

import com.yordanov.warehouse.Exception.ConflictException;
import com.yordanov.warehouse.Exception.ResourceNotFoundException;
import com.yordanov.warehouse.Inventory.Model.Inventory;
import com.yordanov.warehouse.Inventory.Repository.InventoryRepository;
import com.yordanov.warehouse.InventoryMovement.Model.InventoryMovement;
import com.yordanov.warehouse.InventoryMovement.Model.MovementType;
import com.yordanov.warehouse.InventoryMovement.Model.ReferenceType;
import com.yordanov.warehouse.InventoryMovement.Repository.InventoryMovementRepository;
import com.yordanov.warehouse.Product.Model.Product;
import com.yordanov.warehouse.Product.Repository.ProductRepository;
import com.yordanov.warehouse.ReferenceSequence.Service.ReferenceSequenceService;
import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import com.yordanov.warehouse.Warehouse.Repository.WarehouseRepository;
import com.yordanov.warehouse.Web.Dto.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StockService {

    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final ReferenceSequenceService referenceSequenceService;

    public StockService(ProductRepository productRepository, WarehouseRepository warehouseRepository, InventoryRepository inventoryRepository, InventoryMovementRepository inventoryMovementRepository, ReferenceSequenceService referenceSequenceService) {
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryMovementRepository = inventoryMovementRepository;
        this.referenceSequenceService = referenceSequenceService;
    }

    @Transactional
    public ReceiveStockResponse receiveStock(ReceiveStockRequest receiveStockRequest) {

       Product product = productRepository.findById(receiveStockRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
       Warehouse warehouse = warehouseRepository.findById(receiveStockRequest.getWarehouseId()).orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

       LocalDateTime now = LocalDateTime.now();

        Inventory inventory = inventoryRepository
                .findByWarehouseIdAndProductId(warehouse.getId(), product.getId())
                .orElseGet(() -> Inventory.builder()
                        .product(product)
                        .warehouse(warehouse)
                        .quantity(0)
                        .reservedQuantity(0)
                        .createdAt(now)
                        .build()
                );

        inventory.setQuantity(inventory.getQuantity() + receiveStockRequest.getQuantity());
        inventory.setUpdatedAt(now);

        inventoryRepository.save(inventory);

        InventoryMovement inventoryMovement = createInventoryMovement(warehouse,product, receiveStockRequest.getQuantity(), ReferenceType.DELIVERY,MovementType.IN, now);
        inventoryMovementRepository.save(inventoryMovement);

        return ReceiveStockResponse.builder()
                .movementId(inventoryMovement.getId())
                .productId(product.getId())
                .warehouseId(warehouse.getId())
                .receiveDate(inventoryMovement.getCreatedAt())
                .receiveQuantity(receiveStockRequest.getQuantity())
                .newQuantity(inventory.getQuantity())
                .build();
    }

    @Transactional
    public ReserveStockResponse reserveStock(ReserveStockRequest reserveStockRequest) {

        Inventory inventory = findInventoryByWarehouseIdAndProductId(reserveStockRequest.getWarehouseId(),reserveStockRequest.getProductId());

        int availableQuantity = inventory.getAvailableQuantity();

        LocalDateTime now = LocalDateTime.now();

        if(availableQuantity < reserveStockRequest.getQuantity()){
            throw new ConflictException("Not enough quantity to reserve, available is %s".formatted(availableQuantity));
        }
            inventory.setReservedQuantity(inventory.getReservedQuantity() + reserveStockRequest.getQuantity());
            inventory.setUpdatedAt(now);
            inventoryRepository.save(inventory);

        InventoryMovement inventoryMovement = createInventoryMovement(inventory.getWarehouse(), inventory.getProduct(),
                reserveStockRequest.getQuantity(), ReferenceType.ORDER,MovementType.RESERVE, now);

        inventoryMovementRepository.save(inventoryMovement);

        return ReserveStockResponse.builder()
                .movementId(inventoryMovement.getId())
                .productId(inventory.getProduct().getId())
                .warehouseId(inventory.getWarehouse().getId())
                .reserveQuantity(reserveStockRequest.getQuantity())
                .availableQuantity(availableQuantity - reserveStockRequest.getQuantity())
                .reference(inventoryMovement.getReference())
                .referenceType(ReferenceType.ORDER)
                .reserveAt(now)
                .build();
    }

    @Transactional
    public ReleaseStockResponse releaseStock(ReleaseStockRequest releaseStockRequest){

        Inventory inventory = findInventoryByWarehouseIdAndProductId(releaseStockRequest.getWarehouseId(),releaseStockRequest.getProductId());

        if(inventory.getReservedQuantity() < releaseStockRequest.getQuantity()){
            throw new ConflictException("Not enough stock to release");
        }

        LocalDateTime now = LocalDateTime.now();

        inventory.setReservedQuantity(inventory.getReservedQuantity() - releaseStockRequest.getQuantity());
        inventory.setUpdatedAt(now);
        inventoryRepository.save(inventory);

        InventoryMovement inventoryMovement = createInventoryMovement(inventory.getWarehouse(),inventory.getProduct(),
                releaseStockRequest.getQuantity(), ReferenceType.ORDER,MovementType.RELEASE, now);

        inventoryMovementRepository.save(inventoryMovement);

        return ReleaseStockResponse.builder()
                .productId(inventory.getProduct().getId())
                .warehouseId(inventory.getWarehouse().getId())
                .movementId(inventoryMovement.getId())
                .releaseQuantity(releaseStockRequest.getQuantity())
                .reference(inventoryMovement.getReference())
                .referenceType(ReferenceType.ORDER)
                .releaseAt(now)
                .build();
    }

    @Transactional
    public ShipStockResponse shipStock(ShipStockRequest shipStockRequest) {

        Inventory inventory = findInventoryByWarehouseIdAndProductId(
                shipStockRequest.getWarehouseId(),
                shipStockRequest.getProductId()
        );

        if(inventory.getReservedQuantity() < shipStockRequest.getQuantity()){
            throw new ConflictException("Not enough reserved stock to ship");
        }

        LocalDateTime now = LocalDateTime.now();


        inventory.setReservedQuantity(inventory.getReservedQuantity() - shipStockRequest.getQuantity());
        inventory.setQuantity(inventory.getQuantity() - shipStockRequest.getQuantity());
        inventory.setUpdatedAt(now);
        inventoryRepository.save(inventory);

        InventoryMovement inventoryMovement = createInventoryMovement(inventory.getWarehouse(),inventory.getProduct(),
                shipStockRequest.getQuantity(),ReferenceType.ORDER,MovementType.OUT,now);

        inventoryMovementRepository.save(inventoryMovement);

        return ShipStockResponse.builder()
                .movementId(inventoryMovement.getId())
                .productId(inventory.getProduct().getId())
                .warehouseId(inventory.getWarehouse().getId())
                .shipQuantity(shipStockRequest.getQuantity())
                .reference(inventoryMovement.getReference())
                .shipDate(now)
                .build();
    }

    public Inventory findInventoryByWarehouseIdAndProductId(UUID warehouseId,UUID productId){

        return  inventoryRepository.findByWarehouseIdAndProductId(warehouseId,productId)
                .orElseThrow(() -> new ResourceNotFoundException("No such Inventory with productId %s and warehouseId %s"
                        .formatted(productId,warehouseId)));
    }

    private InventoryMovement createInventoryMovement(Warehouse warehouse,Product product,int quantity, ReferenceType referenceType, MovementType movementType, LocalDateTime now){

        return InventoryMovement.builder()
                .warehouse(warehouse)
                .product(product)
                .quantity(quantity)
                .movementType(movementType)
                .referenceType(referenceType)
                .reference(referenceSequenceService.generateReference(referenceType,warehouse.getId()))
                .createdAt(now)
                .build();
    }
}
