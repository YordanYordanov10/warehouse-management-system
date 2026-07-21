package com.yordanov.warehouse.StockService;

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

        Inventory inventory = inventoryRepository
                .findByWarehouseIdAndProductId(warehouse.getId(), product.getId())
                .orElseGet(() -> Inventory.createEmpty(product, warehouse));

        inventory.receive(receiveStockRequest.getQuantity());
        inventoryRepository.save(inventory);

        String reference = referenceSequenceService.generateReference(ReferenceType.DELIVERY, warehouse.getId());
        InventoryMovement inventoryMovement = createInventoryMovement(warehouse,product, receiveStockRequest.getQuantity(), ReferenceType.DELIVERY,MovementType.IN, reference);
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
    public ReserveStockResponse reserveStock(UUID warehouseId, UUID productId, int quantity, String orderReference) {

        Inventory inventory = findInventoryByWarehouseIdAndProductId(warehouseId,productId);

        inventory.reserve(quantity);    
            inventoryRepository.save(inventory);

        InventoryMovement inventoryMovement = createInventoryMovement(inventory.getWarehouse(), inventory.getProduct(),
                quantity, ReferenceType.ORDER, MovementType.RESERVE, orderReference);

        inventoryMovementRepository.save(inventoryMovement);

        return ReserveStockResponse.builder()
                .movementId(inventoryMovement.getId())
                .productId(inventory.getProduct().getId())
                .warehouseId(inventory.getWarehouse().getId())
                .reserveQuantity(quantity)
                .availableQuantity(inventory.getAvailableQuantity())
                .reference(inventoryMovement.getReference())
                .referenceType(ReferenceType.ORDER)
                .reserveAt(inventoryMovement.getCreatedAt())
                .build();
    }

    @Transactional
    public ReleaseStockResponse releaseStock(UUID warehouseId, UUID productId, int quantity, String orderReference ){

        Inventory inventory = findInventoryByWarehouseIdAndProductId(warehouseId, productId);

        inventory.release(quantity);
        inventoryRepository.save(inventory);

        InventoryMovement inventoryMovement = createInventoryMovement(inventory.getWarehouse(),inventory.getProduct(),
                quantity, ReferenceType.ORDER,MovementType.RELEASE, orderReference);

        inventoryMovementRepository.save(inventoryMovement);

        return ReleaseStockResponse.builder()
                .productId(inventory.getProduct().getId())
                .warehouseId(inventory.getWarehouse().getId())
                .movementId(inventoryMovement.getId())
                .releaseQuantity(quantity)
                .reference(inventoryMovement.getReference())
                .referenceType(ReferenceType.ORDER)
                .releaseAt(inventoryMovement.getCreatedAt())
                .build();
    }

    @Transactional
    public ShipStockResponse shipStock(UUID warehouseId, UUID productId, int quantity, String orderReference ) {

        Inventory inventory = findInventoryByWarehouseIdAndProductId(warehouseId, productId);

        inventory.ship(quantity);
        inventoryRepository.save(inventory);

        InventoryMovement inventoryMovement = createInventoryMovement(inventory.getWarehouse(),inventory.getProduct(),
                quantity,ReferenceType.ORDER,MovementType.OUT, orderReference);

        inventoryMovementRepository.save(inventoryMovement);

        return ShipStockResponse.builder()
                .movementId(inventoryMovement.getId())
                .productId(inventory.getProduct().getId())
                .warehouseId(inventory.getWarehouse().getId())
                .shipQuantity(quantity)
                .reference(inventoryMovement.getReference())
                .build();
    }

    public Inventory findInventoryByWarehouseIdAndProductId(UUID warehouseId,UUID productId){

        return  inventoryRepository.findByWarehouseIdAndProductId(warehouseId,productId)
                .orElseThrow(() -> new ResourceNotFoundException("No such Inventory with productId %s and warehouseId %s"
                        .formatted(productId,warehouseId)));
    }

    private InventoryMovement createInventoryMovement(Warehouse warehouse,Product product,int quantity, ReferenceType referenceType, MovementType movementType, String orderReference){

        return InventoryMovement.builder()
                .warehouse(warehouse)
                .product(product)
                .quantity(quantity)
                .movementType(movementType)
                .referenceType(referenceType)
                .reference(orderReference)
                .build();
    }
}
