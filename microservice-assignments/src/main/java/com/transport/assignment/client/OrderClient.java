package com.transport.assignment.client;

import com.transport.assignment.dto.AssignDriverRequest;
import com.transport.assignment.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "ms-orders", url = "${order.micro.url}")
public interface OrderClient {

    @GetMapping("/api/v1/orders/{id}")
    OrderResponse findById(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/orders/{id}/assign-driver")
    OrderResponse assignDriver(@PathVariable("id") UUID id,
                               @RequestBody AssignDriverRequest request);
}
