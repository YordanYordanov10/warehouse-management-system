package com.yordanov.warehouse.Web;

import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import com.yordanov.warehouse.Warehouse.Service.WarehouseService;
import com.yordanov.warehouse.Web.Dto.UpdateWarehouseRequest;
import com.yordanov.warehouse.Web.Dto.WarehouseRequest;
import com.yordanov.warehouse.Web.Dto.WarehouseResponse;
import com.yordanov.warehouse.Web.Mapper.DtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Warehouses", description = "Warehouse management")
@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;


    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Operation(summary = "Get all warehouses",
            description = "Returns all warehouses")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "404", description = "Warehouses not found")
    })
    @GetMapping()
    public ResponseEntity<List<WarehouseResponse>> getAllWarehouses() {

        List<Warehouse> warehouses = warehouseService.findAllWarehouses();
        List<WarehouseResponse> warehouseResponses = DtoMapper.toWarehouseResponseList(warehouses);
        return new ResponseEntity<>(warehouseResponses, HttpStatus.OK);
    }

    @Operation(summary = "Create a new warehouse",
            description = "Creates a new warehouse with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Warehouse created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping()
    public ResponseEntity<WarehouseResponse> createWarehouse(@Valid @RequestBody WarehouseRequest warehouseRequest) {

        Warehouse warehouse = warehouseService.createWarehouse(warehouseRequest);
        WarehouseResponse warehouseResponse = DtoMapper.toWarehouseResponse(warehouse);
        return new ResponseEntity<>(warehouseResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get warehouse by id",
            description = "Returns a warehouse by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable UUID id) {

        Warehouse warehouse = warehouseService.getWarehouseById(id);
        WarehouseResponse warehouseResponse = DtoMapper.toWarehouseResponse(warehouse);
        return new ResponseEntity<>(warehouseResponse, HttpStatus.OK);
    }

    @Operation(summary = "Update warehouse",
            description = "Updates a warehouse by its id with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Warehouse updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable UUID id, @Valid @RequestBody WarehouseRequest warehouseRequest) {

        Warehouse warehouse = warehouseService.updateWarehouse(warehouseRequest,id);
        WarehouseResponse warehouseResponse = DtoMapper.toWarehouseResponse(warehouse);
        return new ResponseEntity<>(warehouseResponse, HttpStatus.OK);
    }

    @Operation(summary = "Change warehouse status",
            description = "Changes the status of a warehouse by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Warehouse status changed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<WarehouseResponse> changeStatusWarehouse(@PathVariable UUID id, @Valid @RequestBody UpdateWarehouseRequest updateWarehouseRequest) {

        Warehouse warehouse = warehouseService.changeStatusWarehouse(id,updateWarehouseRequest);
        WarehouseResponse warehouseResponse = DtoMapper.toWarehouseResponse(warehouse);
        return new ResponseEntity<>(warehouseResponse, HttpStatus.OK);
    }
}
