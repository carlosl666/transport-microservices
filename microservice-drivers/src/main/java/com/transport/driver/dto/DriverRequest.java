package com.transport.driver.dto;

import jakarta.validation.constraints.NotBlank;

public record DriverRequest(
        @NotBlank(message = "El campo name es obligatorio, favor de validar")
        String name,

        @NotBlank(message = "El campo licenseNumber es obligatorio, favor de validar")
        String licenseNumber
) {
}
