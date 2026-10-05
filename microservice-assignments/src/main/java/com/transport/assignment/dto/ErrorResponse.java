package com.transport.assignment.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int statusCode;
    private String path;
    private List<String> detalles;
}
