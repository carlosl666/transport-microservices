package com.transport.order.controller;

import com.transport.order.dto.AssignDriverRequest;
import com.transport.order.dto.OrderRequest;
import com.transport.order.dto.OrderResponse;
import com.transport.order.dto.OrderStatusRequest;
import com.transport.order.entity.OrderStatus;
import com.transport.order.exception.MicroResponseException;
import com.transport.order.service.OrderService;
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

class OrderControllerTest {

    @Mock
    private OrderRequest orderRequest;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private OrderService service;

    @Mock
    private OrderStatusRequest orderStatusRequest;

    @Mock
    private AssignDriverRequest assignDriverRequest;

    @InjectMocks
    private OrderController controller;

    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate1() {
        ResponseEntity<OrderResponse> response = controller.create(orderRequest, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreate2() {
        when(bindingResult.hasErrors()).thenReturn(true);
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> controller.create(orderRequest, bindingResult));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testChangeStatus3() {
        ResponseEntity<OrderResponse> response = controller.changeStatus(id, orderStatusRequest, bindingResult);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testChangeStatus4() {
        when(bindingResult.hasErrors()).thenReturn(true);
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> controller.changeStatus(id, orderStatusRequest, bindingResult));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }


    @Test
    void testassignDriver5() {
        ResponseEntity<OrderResponse> response = controller.assignDriver(id, assignDriverRequest, bindingResult);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testassignDriver6() {
        when(bindingResult.hasErrors()).thenReturn(true);
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> controller.assignDriver(id, assignDriverRequest, bindingResult));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testFindById7() {
        ResponseEntity<OrderResponse> response = controller.findById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindAll8() {
        ResponseEntity<List<OrderResponse>> response = controller.findAll(OrderStatus.CREATED, null, null, null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}