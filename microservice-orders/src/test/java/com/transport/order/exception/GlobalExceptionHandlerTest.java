package com.transport.order.exception;

import com.transport.order.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Mock
    private NoResourceFoundException noResourceFoundException;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException;

    @Mock
    private HttpMessageNotReadableException httpMessageNotReadableException;

    @Mock
    private TypeMismatchException typeMismatchException;

    @Mock
    private MicroResponseException microResponseException;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ServletWebRequest webRequest;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private SQLException sqlException;

    @Mock
    private StackTraceElement traceElement;

    @Mock
    private InvalidDataAccessApiUsageException invalidDataAccessApiUsageException;

    @Mock
    private InvalidDataAccessResourceUsageException invalidDataAccessResourceUsageException;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleNoResourceFoundException1() {
        ResponseEntity<Object> response = globalExceptionHandler.
                handleNoResourceFoundException(noResourceFoundException, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testHandleMethodArgumentNotValid2() {
        ResponseEntity<Object> response = globalExceptionHandler.
                handleMethodArgumentNotValid(methodArgumentNotValidException, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testHandleHttpRequestMethodNotSupported3() {
        when(webRequest.getRequest()).thenReturn(httpServletRequest);
        ResponseEntity<Object> response = globalExceptionHandler.
                handleHttpRequestMethodNotSupported(httpRequestMethodNotSupportedException, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testHandleHttpMessageNotReadable4() {
        when(webRequest.getRequest()).thenReturn(httpServletRequest);
        ResponseEntity<Object> response = globalExceptionHandler.
                handleHttpMessageNotReadable(httpMessageNotReadableException, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testHandleTypeMismatch5() {
        when(webRequest.getRequest()).thenReturn(httpServletRequest);
        ResponseEntity<Object> response = globalExceptionHandler.
                handleTypeMismatch(typeMismatchException, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGenericExceptionHandler6() {
        when(microResponseException.getStatus()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(microResponseException.getMessage()).thenReturn("Internal Server Error");
        when(microResponseException.getBindingResult()).thenReturn(bindingResult);
        when(httpServletRequest.getRequestURI()).thenReturn("/");
        ResponseEntity<Object> response = globalExceptionHandler.
                genericExceptionHandler(microResponseException, httpServletRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testResponseException7() {
        StackTraceElement[] arrayTrace = new StackTraceElement[1];
        arrayTrace[0] = traceElement;
        when(sqlException.getStackTrace()).thenReturn(arrayTrace);
        ErrorResponse errorResponse = globalExceptionHandler.responseException(sqlException, httpServletRequest);
        assertNotNull(errorResponse);
    }

    @Test
    void testResponseException8() {
        StackTraceElement[] arrayTrace = new StackTraceElement[1];
        arrayTrace[0] = traceElement;
        when(invalidDataAccessApiUsageException.getStackTrace()).thenReturn(arrayTrace);
        ErrorResponse errorResponse = globalExceptionHandler.responseException(invalidDataAccessApiUsageException, httpServletRequest);
        assertNotNull(errorResponse);
    }

    @Test
    void testResponseException9() {
        StackTraceElement[] arrayTrace = new StackTraceElement[1];
        arrayTrace[0] = traceElement;
        when(invalidDataAccessResourceUsageException.getStackTrace()).thenReturn(arrayTrace);
        ErrorResponse errorResponse = globalExceptionHandler.responseException(invalidDataAccessResourceUsageException, httpServletRequest);
        assertNotNull(errorResponse);
    }
}