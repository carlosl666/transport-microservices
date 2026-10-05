package com.transport.driver.service;

import com.transport.driver.dto.DriverRequest;
import com.transport.driver.dto.DriverResponse;
import com.transport.driver.entity.Driver;
import com.transport.driver.exception.MicroResponseException;
import com.transport.driver.mapper.DriverMapper;
import com.transport.driver.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository repository;
    private final DriverMapper mapper;

    @Override
    @Transactional
    public DriverResponse create(DriverRequest request) {
        if (this.repository.existsByLicenseNumber(request.licenseNumber())) {
            log.info("El conductor ya existe licenseNumber={}", request.licenseNumber());
            throw MicroResponseException.create("Ya existe un conductor con la licencia: "
                    + request.licenseNumber(), HttpStatus.BAD_REQUEST);
        }
        Driver driver = this.repository.saveAndFlush(this.mapper.toEntity(request));
        log.info("Conductor creado id = {}", driver.getId());
        return this.mapper.toResponse(driver);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverResponse> findActiveDriver() {
        return this.mapper.toResponseList(repository.findByActiveTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public DriverResponse findById(UUID id) {
        return this.repository.findById(id).map(this.mapper::toResponse)
                .orElseThrow(() -> new MicroResponseException("Conductor no encontrado id: " + id, HttpStatus.NOT_FOUND));
    }
}
