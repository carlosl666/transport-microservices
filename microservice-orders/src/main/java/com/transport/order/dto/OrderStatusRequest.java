package com.transport.order.dto;

import com.transport.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(
        @NotNull(message = "El campo status es obligatorio, favor de validar")
        OrderStatus status
) {
}
