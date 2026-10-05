package com.transport.order.service;

import com.transport.order.dto.OrderRequest;
import com.transport.order.dto.OrderResponse;
import com.transport.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse create(OrderRequest request);

    OrderResponse changeStatus(UUID id, OrderStatus status);

    OrderResponse assignDriver(UUID orderId, UUID driverId);

    OrderResponse findById(UUID id);

    List<OrderResponse> findAll(OrderStatus status, LocalDateTime start,
                                LocalDateTime end, String origin, String destination);
}
