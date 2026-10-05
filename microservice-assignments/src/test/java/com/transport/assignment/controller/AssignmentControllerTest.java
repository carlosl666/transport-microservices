package com.transport.assignment.controller;

import com.transport.assignment.dto.AssignmentRequest;
import com.transport.assignment.dto.AssignmentResponse;
import com.transport.assignment.exception.MicroResponseException;
import com.transport.assignment.service.AssignmentService;
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

class AssignmentControllerTest {

    @Mock
    private AssignmentRequest assignmentRequest;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private AssignmentService service;

    @InjectMocks
    private AssignmentController assignmentController;

    private final UUID orderId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignDriver1() {
        ResponseEntity<AssignmentResponse> response = assignmentController.assignDriver(orderId, assignmentRequest, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testAssignDriver2() {
        when(bindingResult.hasErrors()).thenReturn(true);
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> assignmentController.assignDriver(orderId, assignmentRequest, bindingResult));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testAddPdf3() {
        ResponseEntity<AssignmentResponse> response = assignmentController.addPdf(orderId, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddImage4() {
        ResponseEntity<AssignmentResponse> response = assignmentController.addImage(orderId, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindById5() {
        ResponseEntity<List<AssignmentResponse>> response = assignmentController.findById();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}