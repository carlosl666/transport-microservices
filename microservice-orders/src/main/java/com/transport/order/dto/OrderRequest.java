package com.transport.order.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderRequest(
        @NotBlank(message = "El campo origin es obligatorio, favor de validar")
        String origin,

        @NotBlank(message = "El campo destination es obligatorio, favor de validar")
        String destination
) {
}
