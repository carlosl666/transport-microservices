package com.transport.driver.controller;

import com.transport.driver.dto.DriverRequest;
import com.transport.driver.dto.DriverResponse;
import com.transport.driver.exception.MicroResponseException;
import com.transport.driver.service.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DriverControllerTest {

    @Mock
    private BindingResult bindingResult;

    @Mock
    private DriverRequest driverRequest;

    @Mock
    private DriverService service;

    @InjectMocks
    private DriverController driverController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate1() {
        ResponseEntity<DriverResponse> response = driverController.create(driverRequest, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreate2() {
        when(bindingResult.hasErrors()).thenReturn(true);
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> driverController.create(driverRequest, bindingResult));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testFindActive3() {
        ResponseEntity<List<DriverResponse>> response = driverController.findActive();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindById4() {
        UUID orderId = UUID.randomUUID();
        ResponseEntity<DriverResponse> response = driverController.findById(orderId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}