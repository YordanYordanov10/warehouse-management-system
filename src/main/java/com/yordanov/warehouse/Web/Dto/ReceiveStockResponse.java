package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.InventoryMovement.Model.MovementType;
import com.yordanov.warehouse.InventoryMovement.Model.ReferenceType;
import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class ReceiveStockResponse {

   @Schema(description = "Unique identifier of the stock movement", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
   private UUID movementId;

   @Schema(description = "Unique identifier of the warehouse where the stock was received", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
   private UUID warehouseId;

   @Schema(description = "Unique identifier of the product that was received", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
   private UUID productId;

   @Schema(description = "Quantity of stock that was received", example = "10")
   private int receiveQuantity;

   @Schema(description = "New quantity of the product in the warehouse after receiving the stock", example = "50")
   private int newQuantity;

   @Schema(description = "receive date and time of the stock movement", example = "2024-06-15T14:30:00")
   private LocalDateTime receiveDate;

}


