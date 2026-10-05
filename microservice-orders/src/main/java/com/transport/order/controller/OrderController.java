package com.transport.order.controller;

import com.transport.order.dto.AssignDriverRequest;
import com.transport.order.dto.OrderRequest;
import com.transport.order.dto.OrderResponse;
import com.transport.order.dto.OrderStatusRequest;
import com.transport.order.entity.OrderStatus;
import com.transport.order.exception.MicroResponseException;
import com.transport.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ordenes", description = "Gestión de ordenes de transporte")
public class OrderController {

    private final OrderService service;

    @PostMapping
    @Operation(summary = "Crear nueva orden")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request, BindingResult result) {
        if (result.hasErrors()) {
            log.info("Datos de entrada incorrectos create message: {}", result.getAllErrors());
            throw MicroResponseException.create(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.create(request));
    }

    @PostMapping("/{id}/status")
    @Operation(summary = "Cambiar estado de la orden")
    public ResponseEntity<OrderResponse> changeStatus(@PathVariable UUID id,
                                                      @Valid @RequestBody OrderStatusRequest request,
                                                      BindingResult result) {
        if (result.hasErrors()) {
            log.info("Datos de entrada incorrectos changeStatus message: {}", result.getAllErrors());
            throw MicroResponseException.create(result);
        }
        return ResponseEntity.ok(this.service.changeStatus(id, request.status()));
    }

    @PostMapping("/{id}/assign-driver")
    @Operation(summary = "Asignar conductor (endpoint interno para microservice-assignments)")
    public ResponseEntity<OrderResponse> assignDriver(@PathVariable UUID id,
                                                      @Valid @RequestBody AssignDriverRequest request,
                                                      BindingResult result) {
        if (result.hasErrors()) {
            log.info("Datos de entrada incorrectos assignDriver message: {}", result.getAllErrors());
            throw MicroResponseException.create(result);
        }
        return ResponseEntity.ok(this.service.assignDriver(id, request.driverId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar orden por ID")
    public ResponseEntity<OrderResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar ordenes con filtros")
    public ResponseEntity<List<OrderResponse>> findAll(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination) {
        return ResponseEntity.ok(this.service.findAll(status, start, end, origin, destination));
    }
}
