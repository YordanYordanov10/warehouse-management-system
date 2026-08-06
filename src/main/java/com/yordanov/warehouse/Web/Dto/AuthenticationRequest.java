package com.yordanov.warehouse.Web.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @NotBlank
    @Size(min = 6, message = "Username must be at least 6 symbols")
    private String username;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 symbols")
    private String password;
}
