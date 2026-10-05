package com.transport.order.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void test() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatusCode(500);
        errorResponse.setPath("test");
        errorResponse.setDetalles(new ArrayList<>());
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getTimestamp());
        assertEquals(500, errorResponse.getStatusCode());
        assertNotNull(errorResponse.getPath());
        assertNotNull(errorResponse.getDetalles());
    }
}