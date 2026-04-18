package com.yordanov.warehouse.Warehouse.Service;

import com.yordanov.warehouse.Exception.ConflictException;
import com.yordanov.warehouse.Exception.ResourceNotFoundException;
import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import com.yordanov.warehouse.Warehouse.Model.WarehouseStatus;
import com.yordanov.warehouse.Warehouse.Repository.WarehouseRepository;
import com.yordanov.warehouse.Web.Dto.UpdateWarehouseRequest;
import com.yordanov.warehouse.Web.Dto.WarehouseRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;


    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> findAllWarehouses() {

        return warehouseRepository.findAll();
    }

    public Warehouse createWarehouse(WarehouseRequest warehouseRequest) {

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByWarehouseCode(warehouseRequest.getWarehouseCode());

        if (optionalWarehouse.isPresent()) {
            throw new ConflictException("Warehouse with %s code already exists".formatted(warehouseRequest.getWarehouseCode()));
        }

        Warehouse warehouse = Warehouse.builder()
                .name(warehouseRequest.getName())
                .warehouseCode(warehouseRequest.getWarehouseCode())
                .city(warehouseRequest.getCity())
                .country(warehouseRequest.getCountry())
                .address(warehouseRequest.getAddress())
                .maxPalletCapacity(warehouseRequest.getMaxPalletCapacity())
                .postalCode(warehouseRequest.getPostalCode())
                .warehouseStatus(WarehouseStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return warehouseRepository.save(warehouse);
    }

    public Warehouse getWarehouseById(UUID id) {

        return warehouseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Warehouse with id %s not found".formatted(id)));
    }

    public Warehouse updateWarehouse(WarehouseRequest warehouseRequest, UUID id) {

        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Warehouse with id %s not found".formatted(id)));

        warehouse.setCity(warehouseRequest.getCity());
        warehouse.setCountry(warehouseRequest.getCountry());
        warehouse.setAddress(warehouseRequest.getAddress());
        warehouse.setPostalCode(warehouseRequest.getPostalCode());
        warehouse.setMaxPalletCapacity(warehouseRequest.getMaxPalletCapacity());
        warehouse.setWarehouseStatus(warehouse.getWarehouseStatus());
        warehouse.setUpdatedAt(LocalDateTime.now());

        warehouseRepository.save(warehouse);

        return warehouse;
    }

    public Warehouse changeStatusWarehouse(UUID id, UpdateWarehouseRequest updateWarehouseRequest) {

        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Warehouse with id %s not found".formatted(id)));
        warehouse.setWarehouseStatus(updateWarehouseRequest.getStatus());
        warehouse.setUpdatedAt(LocalDateTime.now());

        warehouseRepository.save(warehouse);
        return warehouse;
    }
}
