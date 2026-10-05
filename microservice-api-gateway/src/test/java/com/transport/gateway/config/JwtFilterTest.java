package com.transport.gateway.config;

import com.transport.gateway.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private ServerWebExchange serverWebExchange;

    @Mock
    private GatewayFilterChain gatewayFilterChain;

    @Mock
    private ServerHttpRequest serverHttpRequest;

    @Mock
    private HttpHeaders httpHeaders;

    @Mock
    private ServerHttpResponse serverHttpResponse;

    @Mock
    private DataBufferFactory dataBufferFactory;

    @Mock
    private DataBuffer dataBuffer;

    @Mock
    private ErrorResponse errorResponse;

    @Mock
    private ServerWebExchange.Builder builder;

    @InjectMocks
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtFilter, "secret", "mySecreTtestSecureEnoughForHS256");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFilter1() {
        when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        when(serverWebExchange.getRequest().getURI()).thenReturn(URI.create("/swagger-ui"));
        when(gatewayFilterChain.filter(any())).thenReturn(Mono.empty());
        Mono<Void> mono = jwtFilter.filter(serverWebExchange, gatewayFilterChain);
        assertNotNull(mono);
    }

    @Test
    void testFilter2() {
        when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        when(serverWebExchange.getRequest().getURI()).thenReturn(URI.create("/api/v1"));
        when(serverWebExchange.getRequest().getHeaders()).thenReturn(httpHeaders);
        when(serverWebExchange.getRequest().getHeaders().getFirst(anyString())).thenReturn(HttpHeaders.AUTHORIZATION);
        when(serverWebExchange.getResponse()).thenReturn(serverHttpResponse);
        when(serverWebExchange.getResponse().getHeaders()).thenReturn(httpHeaders);
        when(serverWebExchange.getResponse().bufferFactory()).thenReturn(dataBufferFactory);
        when(dataBufferFactory.wrap(any(byte[].class))).thenReturn(dataBuffer);
        when(serverHttpResponse.writeWith(any())).thenReturn(Mono.just(errorResponse).then());
        Mono<Void> mono = jwtFilter.filter(serverWebExchange, gatewayFilterChain);
        assertNotNull(mono);
    }

    @Test
    void testFilter3() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiJ9.RyRMkgmVIPRNnkDKuVkuop-h37z0hgNKcOyjTnisZdI";
        when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        when(serverWebExchange.getRequest().getURI()).thenReturn(URI.create("/api/v1"));
        when(serverWebExchange.getRequest().getHeaders()).thenReturn(httpHeaders);
        when(serverWebExchange.getRequest().getHeaders().getFirst(anyString())).thenReturn("Bearer " + token);
        when(serverWebExchange.mutate()).thenReturn(builder);
        when(builder.request((Consumer<ServerHttpRequest.Builder>) any())).thenReturn(builder);
        when(builder.build()).thenReturn(serverWebExchange);
        when(gatewayFilterChain.filter(any())).thenReturn(Mono.empty());
        Mono<Void> mono = jwtFilter.filter(serverWebExchange, gatewayFilterChain);
        assertNotNull(mono);
    }

    @Test
    void testOrder4() {
        int order = jwtFilter.getOrder();
        assertEquals(-1.0, order);
    }
}