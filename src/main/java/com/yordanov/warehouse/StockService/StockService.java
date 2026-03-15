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
import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import com.yordanov.warehouse.Warehouse.Repository.WarehouseRepository;
import com.yordanov.warehouse.Web.Dto.ReceiveStockRequest;
import com.yordanov.warehouse.Web.Dto.ReceiveStockResponse;
import com.yordanov.warehouse.Web.Dto.ReserveStockRequest;
import com.yordanov.warehouse.Web.Dto.ReserveStockResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockService {

    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository inventoryMovementRepository;

    public StockService(ProductRepository productRepository, WarehouseRepository warehouseRepository, InventoryRepository inventoryRepository, InventoryMovementRepository inventoryMovementRepository) {
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryMovementRepository = inventoryMovementRepository;
    }

    @Transactional
    public ReceiveStockResponse receiveStock(ReceiveStockRequest receiveStockRequest) {

       Product product = productRepository.findById(receiveStockRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
       Warehouse warehouse = warehouseRepository.findById(receiveStockRequest.getWarehouseId()).orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

       Optional<Inventory> optionalInventory = inventoryRepository.findByWarehouseIdAndProductId(warehouse.getId(),product.getId());

       Inventory inventory = new Inventory();

        if(optionalInventory.isPresent()){
            inventory = optionalInventory.get();
            inventory.setQuantity(inventory.getQuantity() + receiveStockRequest.getQuantity());
            inventory.setUpdatedAt(LocalDateTime.now());

            inventoryRepository.save(inventory);
        }else{
           inventory = Inventory.builder()
                    .product(product)
                    .warehouse(warehouse)
                    .quantity(receiveStockRequest.getQuantity())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            inventoryRepository.save(inventory);
        }

        InventoryMovement inventoryMovement =InventoryMovement.builder()
                .movementType(MovementType.IN)
                .reference(receiveStockRequest.getReference())
                .referenceType(ReferenceType.DELIVERY)
                .createdAt(LocalDateTime.now())
                .quantity(receiveStockRequest.getQuantity())
                .product(product)
                .warehouse(warehouse)
                .build();

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

        Product product = productRepository.findById(reserveStockRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Warehouse warehouse = warehouseRepository.findById(reserveStockRequest.getWarehouseId()).orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        Inventory inventory = inventoryRepository.findByWarehouseIdAndProductId(warehouse.getId(), product.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No such Inventory with productId %s and warehouseId %s"
                        .formatted(reserveStockRequest.getProductId(), reserveStockRequest.getWarehouseId())));

        int availableQuantity = inventory.getAvailableQuantity();

        LocalDateTime now = LocalDateTime.now();

        if(availableQuantity < reserveStockRequest.getQuantity()){
            throw new ConflictException("Not enough quantity to reserve, available is %s".formatted(availableQuantity));
        }
            inventory.setReservedQuantity(inventory.getReservedQuantity() + reserveStockRequest.getQuantity());
            inventory.setUpdatedAt(now);
            inventoryRepository.save(inventory);

            InventoryMovement inventoryMovement = InventoryMovement.builder()
                    .product(product)
                    .warehouse(warehouse)
                    .quantity(reserveStockRequest.getQuantity())
                    .reference(reserveStockRequest.getReference())
                    .referenceType(reserveStockRequest.getReferenceType())
                    .movementType(MovementType.RESERVE)
                    .createdAt(now)
                    .build();

            inventoryMovementRepository.save(inventoryMovement);

        return ReserveStockResponse.builder()
                .movementId(inventoryMovement.getId())
                .productId(product.getId())
                .warehouseId(warehouse.getId())
                .reserveQuantity(reserveStockRequest.getQuantity())
                .availableQuantity(availableQuantity - reserveStockRequest.getQuantity())
                .reference(reserveStockRequest.getReference())
                .referenceType(reserveStockRequest.getReferenceType())
                .reserveAt(now)
                .build();

    }
}
