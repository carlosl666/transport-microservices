package com.transport.assignment.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String status,
        String origin,
        String destination,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID driverId
) {
}
