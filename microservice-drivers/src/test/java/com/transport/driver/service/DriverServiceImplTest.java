package com.transport.driver.service;

import com.transport.driver.dto.DriverRequest;
import com.transport.driver.dto.DriverResponse;
import com.transport.driver.entity.Driver;
import com.transport.driver.exception.MicroResponseException;
import com.transport.driver.mapper.DriverMapper;
import com.transport.driver.repository.DriverRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @Mock
    private DriverRequest driverRequest;

    @Mock
    private DriverRepository repository;

    @Mock
    private DriverMapper mapper;

    @Mock
    private Driver driver;

    @Mock
    private DriverResponse driverResponse;

    @InjectMocks
    private DriverServiceImpl service;

    @Test
    void testCreate1() {
        when(driverRequest.licenseNumber()).thenReturn("1234567890");
        when(repository.existsByLicenseNumber(anyString())).thenReturn(true);
        MicroResponseException exception = assertThrows(MicroResponseException.class,
                () -> service.create(driverRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testCreate2() {
        when(driverRequest.licenseNumber()).thenReturn("1234567890");
        when(repository.existsByLicenseNumber(anyString())).thenReturn(false);
        when(mapper.toEntity(any())).thenReturn(driver);
        when(repository.saveAndFlush(any())).thenReturn(driver);
        when(mapper.toResponse(any())).thenReturn(driverResponse);
        DriverResponse response = service.create(driverRequest);
        assertNotNull(response);
    }

    @Test
    void testFindActiveDriver3() {
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        List<DriverResponse> driverResponses = new ArrayList<>();
        driverResponses.add(driverResponse);
        when(repository.findByActiveTrue()).thenReturn(drivers);
        when(mapper.toResponseList(anyList())).thenReturn(driverResponses);
        List<DriverResponse> response = service.findActiveDriver();
        assertNotNull(response);
    }

    @Test
    void findById() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(driver));
        when(mapper.toResponse(any())).thenReturn(driverResponse);
        DriverResponse response = service.findById(UUID.randomUUID());
        assertNotNull(response);
    }
}