package com.transport.assignment.controller;

import com.transport.assignment.dto.AssignmentRequest;
import com.transport.assignment.dto.AssignmentResponse;
import com.transport.assignment.exception.MicroResponseException;
import com.transport.assignment.service.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
@Tag(name = "Asignaciones", description = "Asignación de conductores y archivos")
@Slf4j
public class AssignmentController {

    private final AssignmentService service;

    @PostMapping("/orders/{orderId}")
    @Operation(summary = "Asignar un conductor a una orden")
    public ResponseEntity<AssignmentResponse> assignDriver(@PathVariable UUID orderId,
                                                           @Valid @RequestBody AssignmentRequest request,
                                                           BindingResult result) {
        if (result.hasErrors()) {
            log.info("Datos de entrada incorrectos message: {}", result.getAllErrors());
            throw MicroResponseException.create(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.assignDriver(orderId, request));
    }

    @PostMapping(value = "/{assignmentId}/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Adjuntar PDF a la asignación")
    public ResponseEntity<AssignmentResponse> addPdf(@PathVariable UUID assignmentId,
                                                     @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(this.service.addPdf(assignmentId, file));
    }

    @PostMapping(value = "/{assignmentId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Adjuntar imagen PNG/JPG a la asignación")
    public ResponseEntity<AssignmentResponse> addImage(@PathVariable UUID assignmentId,
                                                       @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(this.service.addImage(assignmentId, file));
    }

    @GetMapping
    @Operation(summary = "Litar el ID de todas las asignaciones")
    public ResponseEntity<List<AssignmentResponse>> findById() {
        return ResponseEntity.ok(this.service.findAll());
    }
}
