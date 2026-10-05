package com.transport.gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired(required = false)
    private SecurityWebFilterChain securityWebFilterChain;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void springSecurityFilterChain() {
        webTestClient.get()
                .uri("/api/v1/drivers/active")
                .exchange()
                .expectStatus().isNotFound();
    }
}