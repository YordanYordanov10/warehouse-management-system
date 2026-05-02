package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard API error response")
public record ErrorResponse(

        @Schema(description = "HTTP status code", example = "400")
        int status,
        @Schema(description = "Human-readable error message", example = "Validation failed")
        String message,
        @Schema(description = "Request path that caused the error", example = "/api/orders/import")
        String path,
        @Schema(description = "Timestamp when the error occurred")
        LocalDateTime timestamp)
{ }
