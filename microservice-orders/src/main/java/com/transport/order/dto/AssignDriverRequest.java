package com.transport.order.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignDriverRequest(
        @NotNull(message = "El ID del conductor es obligatorio o el ID es incorrecto campo driverId, " +
                "favor de validar")
        UUID driverId
) {
}
