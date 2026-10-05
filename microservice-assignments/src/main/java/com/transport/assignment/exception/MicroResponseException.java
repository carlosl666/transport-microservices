package com.transport.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class MicroResponseException extends MicroServiceException {

    public MicroResponseException(String message, HttpStatus status) {
        super(message, status);
    }

    public MicroResponseException(BindingResult bindingResult, HttpStatus status) {
        super(bindingResult, status);
    }

    public MicroResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MicroResponseException(String message) {
        super(message);
    }

    public static MicroResponseException create(BindingResult result) {
        return create(null, result, null);
    }

    public static MicroResponseException create(String message, HttpStatus status) {
        return create(message, null, status);
    }


    public static MicroResponseException create(String message,
                                                BindingResult result,
                                                HttpStatus status) {
        if (result != null) {
            return new MicroResponseException(result, HttpStatus.BAD_REQUEST);
        }
        return new MicroResponseException(message, status);
    }
}
