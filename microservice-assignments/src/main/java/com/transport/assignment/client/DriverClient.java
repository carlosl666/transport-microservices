package com.transport.assignment.client;

import com.transport.assignment.dto.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ms-drivers", url = "${driver.micro.url}")
public interface DriverClient {

    @GetMapping("/api/v1/drivers/{id}")
    DriverResponse findById(@PathVariable("id") UUID id);
}
