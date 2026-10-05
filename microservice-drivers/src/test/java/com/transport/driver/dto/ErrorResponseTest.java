package com.transport.driver.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void test1() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatusCode(400);
        errorResponse.setPath("test");
        errorResponse.setDetalles(new ArrayList<>());
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getTimestamp());
        assertNotNull(errorResponse.getPath());
        assertNotNull(errorResponse.getDetalles());
    }
}