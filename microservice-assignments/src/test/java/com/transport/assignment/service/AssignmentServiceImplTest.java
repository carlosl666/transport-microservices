package com.transport.assignment.service;

import com.transport.assignment.client.DriverClient;
import com.transport.assignment.client.OrderClient;
import com.transport.assignment.dto.AssignmentRequest;
import com.transport.assignment.dto.AssignmentResponse;
import com.transport.assignment.dto.DriverResponse;
import com.transport.assignment.dto.OrderResponse;
import com.transport.assignment.entity.Assignment;
import com.transport.assignment.exception.MicroResponseException;
import com.transport.assignment.mapper.AssignmentMapper;
import com.transport.assignment.repository.AssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceImplTest {

    @Mock
    private AssignmentRepository repository;

    @Mock
    private AssignmentMapper mapper;

    @Mock
    private OrderClient orderClient;

    @Mock
    private DriverClient driverClient;

    @Mock
    private OrderResponse orderResponse;

    @Mock
    private DriverResponse driverResponse;

    @Mock
    private AssignmentRequest assignmentRequest;

    @Mock
    private Assignment assignment;

    @Mock
    private AssignmentResponse assignmentResponse;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    private final UUID orderId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(assignmentService, "uploadDir", "/test");
        //MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignDriver1() {
        when(orderClient.findById(orderId)).thenReturn(orderResponse);
        when(orderResponse.status()).thenReturn("TEST");
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> assignmentService.assignDriver(orderId, assignmentRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testAssignDriver2() {
        when(orderClient.findById(orderId)).thenReturn(orderResponse);
        when(orderResponse.status()).thenReturn("CREATED");
        when(orderResponse.driverId()).thenReturn(orderId);
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> assignmentService.assignDriver(orderId, assignmentRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testAssignDriver3() {
        when(assignmentRequest.driverId()).thenReturn(orderId);
        when(orderClient.findById(orderId)).thenReturn(orderResponse);
        when(orderResponse.status()).thenReturn("CREATED");
        when(driverClient.findById(orderId)).thenReturn(driverResponse);
        when(driverResponse.active()).thenReturn(false);
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> assignmentService.assignDriver(orderId, assignmentRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testAssignDriver4() {
        when(assignmentRequest.driverId()).thenReturn(orderId);
        when(orderClient.findById(orderId)).thenReturn(orderResponse);
        when(orderResponse.status()).thenReturn("CREATED");
        when(driverClient.findById(orderId)).thenReturn(driverResponse);
        when(driverResponse.active()).thenReturn(true);
        when(repository.existsByOrderId(any())).thenReturn(true);
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> assignmentService.assignDriver(orderId, assignmentRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testAssignDriverOk5() {
        when(assignmentRequest.driverId()).thenReturn(orderId);
        when(orderClient.findById(orderId)).thenReturn(orderResponse);
        when(orderResponse.status()).thenReturn("CREATED");
        when(driverClient.findById(orderId)).thenReturn(driverResponse);
        when(driverResponse.active()).thenReturn(true);
        when(repository.existsByOrderId(any())).thenReturn(false);
        when(repository.saveAndFlush(any())).thenReturn(assignment);
        when(mapper.toResponse(any())).thenReturn(assignmentResponse);
        AssignmentResponse response = assignmentService.assignDriver(orderId, assignmentRequest);
        assertNotNull(response);
    }

    @Test
    void testAddPdf6() {
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> assignmentService.addPdf(orderId, multipartFile));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }

    @Test
    void testAddPdf7() {
        Assignment assignment1 = new Assignment();
        assignment1.setId(UUID.randomUUID());
        MockMultipartFile file = new MockMultipartFile(
                "file", "documento.pdf", "application/pdf", "contenido pdf".getBytes()
        );
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(assignment1));
        when(repository.saveAndFlush(any(Assignment.class))).thenReturn(assignment1);
        when(mapper.toResponse(any(Assignment.class))).thenReturn(assignmentResponse);
        AssignmentResponse response = assignmentService.addPdf(UUID.randomUUID(), file);
        assertNotNull(response);
    }

    @Test
    void testAddImage8() {
        Assignment assignment1 = new Assignment();
        assignment1.setId(UUID.randomUUID());
        MockMultipartFile file = new MockMultipartFile(
                "file", "imagen.png", "image/png", "bytes de imagen".getBytes()
        );
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(assignment1));
        when(repository.saveAndFlush(any(Assignment.class))).thenReturn(assignment1);
        when(mapper.toResponse(any(Assignment.class))).thenReturn(assignmentResponse);
        AssignmentResponse response = assignmentService.addImage(UUID.randomUUID(), file);
        assertNotNull(response);
    }

    @Test
    void testFindAll9() {
        List<AssignmentResponse> response = assignmentService.findAll();
        assertNotNull(response);
    }
}