package com.yordanov.warehouse.Web;

import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import com.yordanov.warehouse.Warehouse.Service.WarehouseService;
import com.yordanov.warehouse.Web.Dto.UpdateWarehouseRequest;
import com.yordanov.warehouse.Web.Dto.WarehouseRequest;
import com.yordanov.warehouse.Web.Dto.WarehouseResponse;
import com.yordanov.warehouse.Web.Mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class WarehouseController {

    private final WarehouseService warehouseService;


    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/warehouses")
    public ResponseEntity<List<WarehouseResponse>> getAllWarehouses() {

        List<Warehouse> warehouses = warehouseService.findAllWarehouses();
        List<WarehouseResponse> warehouseResponses = DtoMapper.toWarehouseResponseList(warehouses);
        return new ResponseEntity<>(warehouseResponses, HttpStatus.OK);
    }

    @PostMapping("/warehouses")
    public ResponseEntity<WarehouseResponse> createWarehouse(@Valid @RequestBody WarehouseRequest warehouseRequest) {

        Warehouse warehouse = warehouseService.createWarehouse(warehouseRequest);
        WarehouseResponse warehouseResponse = DtoMapper.toWarehouseResponse(warehouse);
        return new ResponseEntity<>(warehouseResponse, HttpStatus.CREATED);
    }

    @GetMapping("/warehouses/{id}")
    public ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable UUID id) {

        Warehouse warehouse = warehouseService.getWarehouseById(id);
        WarehouseResponse warehouseResponse = DtoMapper.toWarehouseResponse(warehouse);
        return new ResponseEntity<>(warehouseResponse, HttpStatus.OK);
    }

    @PutMapping("/warehouses/{id}")
    public ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable UUID id, @Valid @RequestBody WarehouseRequest warehouseRequest) {

        Warehouse warehouse = warehouseService.updateWarehouse(warehouseRequest,id);
        WarehouseResponse warehouseResponse = DtoMapper.toWarehouseResponse(warehouse);
        return new ResponseEntity<>(warehouseResponse, HttpStatus.OK);
    }

    @PatchMapping("/warehouses/{id}")
    public ResponseEntity<WarehouseResponse> changeStatusWarehouse(@PathVariable UUID id, @Valid @RequestBody UpdateWarehouseRequest updateWarehouseRequest) {

        Warehouse warehouse = warehouseService.changeStatusWarehouse(id,updateWarehouseRequest);
        WarehouseResponse warehouseResponse = DtoMapper.toWarehouseResponse(warehouse);
        return new ResponseEntity<>(warehouseResponse, HttpStatus.OK);
    }
}
