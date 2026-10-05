package com.transport.driver.service;

import com.transport.driver.dto.DriverRequest;
import com.transport.driver.dto.DriverResponse;

import java.util.List;
import java.util.UUID;

public interface DriverService {
    DriverResponse create(DriverRequest request);

    List<DriverResponse> findActiveDriver();

    DriverResponse findById(UUID id);
}
