package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Schema(description = "Request to cancel an order")
@Data
public class CancelOrderRequest {

    @Schema(description = "Reason for cancellation", example = "Customer changed their mind")
    @NotBlank
    private String reason;
}