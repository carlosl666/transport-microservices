package com.transport.order.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void test1() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setStatus(OrderStatus.CREATED);
        order.setOrigin("test");
        order.setDestination("test");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setDriverId(UUID.randomUUID());
        assertNotNull(order);
        assertNotNull(order.getId());
        assertNotNull(order.getStatus());
        assertNotNull(order.getOrigin());
        assertNotNull(order.getDestination());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
        assertNotNull(order.getDriverId());
    }
}