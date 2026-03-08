package com.yordanov.warehouse.Web;

import com.yordanov.warehouse.Inventory.Service.InventoryService;
import com.yordanov.warehouse.InventoryMovement.Model.InventoryMovement;
import com.yordanov.warehouse.StockService.StockService;
import com.yordanov.warehouse.Web.Dto.ReceiveStockRequest;
import com.yordanov.warehouse.Web.Dto.ReceiveStockResponse;
import com.yordanov.warehouse.Web.Mapper.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class StockServiceController {

    private final StockService stockService;
    private final InventoryService inventoryService;

    public StockServiceController(StockService stockService, InventoryService inventoryService) {
        this.stockService = stockService;
        this.inventoryService = inventoryService;
    }

    @PostMapping("receive")
    public ResponseEntity<ReceiveStockResponse> receiveStock(@RequestBody ReceiveStockRequest request) {

        ReceiveStockResponse response = stockService.receiveStock(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
