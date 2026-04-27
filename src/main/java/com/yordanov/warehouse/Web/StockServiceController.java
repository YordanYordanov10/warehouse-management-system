package com.yordanov.warehouse.Web;

import com.yordanov.warehouse.Inventory.Service.InventoryService;
import com.yordanov.warehouse.StockService.StockService;
import com.yordanov.warehouse.Web.Dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Stock Service", description = "Endpoints for managing stock operations")
@RestController
@RequestMapping("/api/stocks")
public class StockServiceController {

    private final StockService stockService;


    public StockServiceController(StockService stockService, InventoryService inventoryService) {
        this.stockService = stockService;

    }

    @Operation(summary = "Receive stock",
            description = "Receives stock for a specific product and warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock received successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product or warehouse not found")
    })
    @PostMapping("/receive")
    public ResponseEntity<ReceiveStockResponse> receiveStock(@Valid @RequestBody ReceiveStockRequest request) {

        ReceiveStockResponse response = stockService.receiveStock(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Reserve stock",
            description = "Reserves stock for a specific product and warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock reserved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product or warehouse not found"),
            @ApiResponse(responseCode = "409", description = "Insufficient stock available")
    })
    @PostMapping("/reserve")
    public ResponseEntity<ReserveStockResponse> reserveStock(@Valid @RequestBody ReserveStockRequest request) {

        ReserveStockResponse response = stockService.reserveStock(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Release stock",
            description = "Releases reserved stock for a specific product and warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock released successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product or warehouse not found"),
            @ApiResponse(responseCode = "409", description = "No reserved stock to release")
    })
    @PostMapping("/release")
    public ResponseEntity<ReleaseStockResponse> releaseStock(@Valid @RequestBody ReleaseStockRequest request) {

        ReleaseStockResponse response = stockService.releaseStock(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Ship stock",
            description = "Ships reserved stock for a specific product and warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock shipped successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product or warehouse not found"),
            @ApiResponse(responseCode = "409", description = "No reserved stock to ship")
    })
    @PostMapping("/ship")
    public ResponseEntity<ShipStockResponse> shipStock(@Valid @RequestBody ShipStockRequest request) {

        ShipStockResponse response = stockService.shipStock(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
