package com.transport.driver.controller;

import com.transport.driver.dto.DriverRequest;
import com.transport.driver.dto.DriverResponse;
import com.transport.driver.exception.MicroResponseException;
import com.transport.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Conductores", description = "Gestión de conductores")
public class DriverController {

    private final DriverService service;

    @PostMapping
    @Operation(summary = "Crear un conductor")
    public ResponseEntity<DriverResponse> create(@Valid @RequestBody DriverRequest request,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            log.info("Datos de entrada incorrectos message: {}", result.getAllErrors());
            throw MicroResponseException.create(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.create(request));
    }

    @GetMapping("/active")
    @Operation(summary = "Listar conductores activos")
    public ResponseEntity<List<DriverResponse>> findActive() {
        return ResponseEntity.ok(this.service.findActiveDriver());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar conductor por ID")
    public ResponseEntity<DriverResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.service.findById(id));
    }
}
