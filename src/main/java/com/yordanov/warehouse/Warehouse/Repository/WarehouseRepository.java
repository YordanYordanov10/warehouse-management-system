package com.yordanov.warehouse.Warehouse.Repository;

import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {


    Optional<Warehouse> findByWarehouseCode(String warehouseCode);

    Optional<Warehouse> findById(UUID warehouseId);
}
