package com.transport.order.service;

import com.transport.order.dto.OrderRequest;
import com.transport.order.dto.OrderResponse;
import com.transport.order.entity.Order;
import com.transport.order.entity.OrderStatus;
import com.transport.order.exception.MicroResponseException;
import com.transport.order.mapper.OrderMapper;
import com.transport.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private OrderMapper mapper;

    @Mock
    private OrderRequest orderRequest;

    @Mock
    private Order order;

    @Mock
    private OrderResponse orderResponse;

    @InjectMocks
    private OrderServiceImpl service;

    private final UUID id = UUID.randomUUID();

    @Test
    void testCreate1() {
        when(mapper.toEntity(any())).thenReturn(order);
        when(repository.saveAndFlush(any())).thenReturn(order);
        when(mapper.toResponse(any())).thenReturn(orderResponse);
        OrderResponse response = service.create(orderRequest);
        assertNotNull(response);
    }

    @Test
    void testChangeStatus2() {
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> service.changeStatus(id, OrderStatus.CREATED));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testChangeStatus3() {
        when(order.getStatus()).thenReturn(OrderStatus.CANCELLED);
        when(repository.findById(any())).thenReturn(Optional.of(order));
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> service.changeStatus(id, OrderStatus.IN_TRANSIT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testChangeStatus4() {
        when(order.getStatus()).thenReturn(OrderStatus.CREATED);
        when(repository.findById(any())).thenReturn(Optional.of(order));
        when(repository.saveAndFlush(any())).thenReturn(order);
        when(mapper.toResponse(any())).thenReturn(orderResponse);
        OrderResponse response = service.changeStatus(id, OrderStatus.IN_TRANSIT);
        assertNotNull(response);
    }

    @Test
    void testAssignDriver5() {
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> service.assignDriver(id, id));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testAssignDriver6() {
        when(order.getStatus()).thenReturn(OrderStatus.CANCELLED);
        when(repository.findById(any())).thenReturn(Optional.of(order));
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> service.assignDriver(id, id));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testAssignDriver7() {
        when(order.getStatus()).thenReturn(OrderStatus.CREATED);
        when(order.getDriverId()).thenReturn(id);
        when(repository.findById(any())).thenReturn(Optional.of(order));
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> service.assignDriver(id, id));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testAssignDriver8() {
        when(order.getStatus()).thenReturn(OrderStatus.CREATED);
        when(repository.findById(any())).thenReturn(Optional.of(order));
        when(repository.saveAndFlush(any())).thenReturn(order);
        when(mapper.toResponse(any())).thenReturn(orderResponse);
        OrderResponse response = service.assignDriver(id, id);
        assertNotNull(response);
    }

    @Test
    void testFindById9() {
        when(repository.findById(any())).thenReturn(Optional.of(order));
        when(mapper.toResponse(any())).thenReturn(orderResponse);
        OrderResponse response = service.findById(id);
        assertNotNull(response);
    }

    @Test
    void testFindAll10() {
        List<OrderResponse> orderResponses = new ArrayList<>();
        orderResponses.add(orderResponse);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(repository.findWithFilters(any(), any(), any(), any(), any())).thenReturn(orders);
        when(mapper.toResponseList(any())).thenReturn(orderResponses);
        List<OrderResponse> response = service.findAll(OrderStatus.CREATED, null, null, null, null);
        assertNotNull(response);
    }
}