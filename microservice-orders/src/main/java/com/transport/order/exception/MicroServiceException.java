package com.transport.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@Getter
public class MicroServiceException extends RuntimeException {

    private final transient BindingResult bindingResult;
    private final HttpStatus status;

    protected MicroServiceException(String message, HttpStatus status) {
        this(message, null, status, null);
    }

    protected MicroServiceException(BindingResult bindingResult, HttpStatus status) {
        this(status.name(), bindingResult, status, null);
    }

    protected MicroServiceException(String message, Throwable cause) {
        this(message, null, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    protected MicroServiceException(String message) {
        this(message, null, null, null);
    }

    public MicroServiceException(String message, BindingResult bindingResult, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.bindingResult = bindingResult;
        this.status = status;
    }
}
