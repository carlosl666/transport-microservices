package com.transport.assignment.service;

import com.transport.assignment.client.DriverClient;
import com.transport.assignment.client.OrderClient;
import com.transport.assignment.dto.*;
import com.transport.assignment.entity.Assignment;
import com.transport.assignment.exception.GlobalExceptionHandler;
import com.transport.assignment.exception.MicroResponseException;
import com.transport.assignment.mapper.AssignmentMapper;
import com.transport.assignment.repository.AssignmentRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    @Value("${file.upload.dir}")
    private String uploadDir;

    private final AssignmentRepository repository;
    private final AssignmentMapper mapper;
    private final OrderClient orderClient;
    private final DriverClient driverClient;

    @Override
    public AssignmentResponse assignDriver(UUID orderId, AssignmentRequest request) {
        OrderResponse order = fetchOrder(orderId);
        if (!"CREATED".equals(order.status())) {
            log.info("Solo se pueden asignar conductores a ordenes con estatus en CREATED: {}", order.status());
            throw new MicroResponseException("Solo se pueden asignar conductores a ordenes CREATED",
                    HttpStatus.BAD_REQUEST);
        }
        if (order.driverId() != null) {
            log.info("La orden ya tiene un conductor asignado: {}", order.driverId());
            throw new MicroResponseException("La orden ya tiene un conductor asignado",
                    HttpStatus.BAD_REQUEST);
        }

        DriverResponse driver = fetchDriver(request.driverId());
        if (!driver.active()) {
            log.info("El conductor no esta activo, favor de validar");
            throw new MicroResponseException("El conductor no está activo", HttpStatus.BAD_REQUEST);
        }

        if (this.repository.existsByOrderId(orderId)) {
            log.info("La orden ya tiene asignacion");
            throw new MicroResponseException("La orden ya tiene asignación registrada", HttpStatus.BAD_REQUEST);
        }

        Assignment assignment = Assignment.builder()
                .orderId(orderId)
                .driverId(driver.id())
                .driverName(driver.name())
                .build();
        Assignment saved = this.repository.saveAndFlush(assignment);

        try {
            orderClient.assignDriver(orderId, new AssignDriverRequest(driver.id()));
        } catch (FeignException e) {
            log.error("Error al notificar a ms-orders: POST /api/v1/orders/{id}/assign-driver - {}",
                    GlobalExceptionHandler.msgError(e));
            throw new MicroResponseException("No se pudo vincular el conductor a la orden");
        }

        log.info("Asignación creada id={} order={} driver={}", saved.getId(), orderId, driver.id());
        return this.mapper.toResponse(saved);
    }

    @Override
    public AssignmentResponse addPdf(UUID assignmentId, MultipartFile file) {
        if (file.isEmpty() || !"application/pdf".equals(file.getContentType())) {
            log.info("Tipo de archivo inválido. Se esperaba: application/pdf");
            throw new MicroResponseException("Tipo de archivo inválido. Se esperaba: application/pdf");
        }
        Assignment assignment = findAssignment(assignmentId);
        assignment.setPdfFilePath(store(file, "pdf"));
        return this.mapper.toResponse(this.repository.saveAndFlush(assignment));
    }

    @Override
    public AssignmentResponse addImage(UUID assignmentId, MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/png") || contentType.equals("image/jpeg"))) {
            log.info("Tipo de imagen inválido");
            throw new MicroResponseException("Tipo de imagen inválido. Se esperaba PNG o JPG");
        }
        Assignment assignment = findAssignment(assignmentId);
        assignment.setImageFilePath(store(file, "image"));
        return this.mapper.toResponse(this.repository.saveAndFlush(assignment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponse> findAll() {
        return this.mapper.toResponseList(this.repository.findAll());
    }

    private Assignment findAssignment(UUID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new MicroResponseException("Asignación no encontrada: " + id,
                        HttpStatus.NOT_FOUND));
    }

    private OrderResponse fetchOrder(UUID orderId) {
        try {
            return orderClient.findById(orderId);
        } catch (FeignException.NotFound e) {
            throw new MicroResponseException("Orden no encontrada: " + orderId);
        } catch (FeignException e) {
            log.error("Error al comunicarse con ms-orders GET /api/v1/orders/{id}: {}",
                    GlobalExceptionHandler.msgError(e));
            throw new MicroResponseException("Servicio de órdenes no disponible");
        }
    }

    private DriverResponse fetchDriver(UUID driverId) {
        try {
            return driverClient.findById(driverId);
        } catch (FeignException.NotFound e) {
            throw new MicroResponseException("Conductor no encontrado: " + driverId);
        } catch (FeignException e) {
            log.error("Error al comunicarse con ms-drivers GET /api/v1/drivers/{id}: {}",
                    GlobalExceptionHandler.msgError(e));
            throw new MicroResponseException("Servicio de conductores no disponible");
        }
    }

    private String store(MultipartFile file, String prefix) {
        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            String filename = prefix + "_" + file.getOriginalFilename();
            Path target = dir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            log.info("Archivo almacenado en {}", target);
            return target.toString();
        } catch (IOException e) {
            log.error("Error archivo store: {}", GlobalExceptionHandler.msgError(e));
            throw new MicroResponseException("Error al almacenar el archivo: " + e.getMessage());
        }
    }
}
