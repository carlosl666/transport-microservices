package com.transport.assignment.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignmentRequest(
        @NotNull(message = "El ID del conductor es obligatorio campo driverId, favor de validar")
        UUID driverId
) {
}
