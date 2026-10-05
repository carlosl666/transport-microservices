package com.transport.assignment.dto;

import java.util.UUID;

public record AssignmentResponse(
        UUID id,
        UUID orderId,
        UUID driverId,
        String driverName,
        String pdfFilePath,
        String imageFilePath
) {
}
