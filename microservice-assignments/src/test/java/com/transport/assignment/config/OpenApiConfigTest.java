package com.transport.assignment.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @InjectMocks
    private OpenApiConfig openApiConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCustomOpenAPI1() {
        OpenAPI openAPI = openApiConfig.customOpenAPI();
        assertNotNull(openAPI);
    }
}