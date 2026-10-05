package com.transport.order.service;

import com.transport.order.dto.OrderRequest;
import com.transport.order.dto.OrderResponse;
import com.transport.order.entity.Order;
import com.transport.order.entity.OrderStatus;
import com.transport.order.exception.MicroResponseException;
import com.transport.order.mapper.OrderMapper;
import com.transport.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public OrderResponse create(OrderRequest request) {
        Order order = this.mapper.toEntity(request);
        order.setStatus(OrderStatus.CREATED);
        Order order1 = this.repository.saveAndFlush(order);
        log.info("Orden creada id={} status={}", order1.getId(), order1.getStatus());
        return this.mapper.toResponse(order1);
    }

    @Override
    public OrderResponse changeStatus(UUID id, OrderStatus status) {
        Order order = this.repository.findById(id)
                .orElseThrow(() -> new MicroResponseException("Orden no encontrada para el cambio de estatus: " + id,
                        HttpStatus.NOT_FOUND));

        if (!order.getStatus().validSts(status)) {
            log.info("La orden tine un estatus invalido");
            throw new MicroResponseException(
                    "Transición inválida de " + order.getStatus() + " a " + status, HttpStatus.BAD_REQUEST);
        }
        order.setStatus(status);
        Order updated = this.repository.saveAndFlush(order);
        log.info("Orden {} cambiada a {}", id, status);
        return this.mapper.toResponse(updated);
    }

    @Override
    public OrderResponse assignDriver(UUID orderId, UUID driverId) {
        Order order = this.repository.findById(orderId)
                .orElseThrow(() -> new MicroResponseException("Orden no encontrada para la asignacion: " + orderId,
                        HttpStatus.NOT_FOUND));

        if (order.getStatus() != OrderStatus.CREATED) {
            log.info("Solo se pueden asignar conductores a órdenes con estatus de CREATED");
            throw new MicroResponseException("Solo se pueden asignar conductores a órdenes con estatus de CREATED",
                    HttpStatus.BAD_REQUEST);
        }
        if (order.getDriverId() != null) {
            log.info("La orden ya tiene un conductor asignado");
            throw new MicroResponseException("La orden ya tiene un conductor asignado",
                    HttpStatus.BAD_REQUEST);
        }
        order.setDriverId(driverId);
        Order updated = this.repository.saveAndFlush(order);
        log.info("Conductor {} asignado a orden {}", driverId, orderId);
        return this.mapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(UUID id) {
        return this.repository.findById(id).map(this.mapper::toResponse)
                .orElseThrow(() -> new MicroResponseException("Orden no encontrada: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(OrderStatus status, LocalDateTime start, LocalDateTime end, String origin, String destination) {
        return this.mapper.toResponseList(
                this.repository.findWithFilters(status, start, end, origin, destination));
    }
}
