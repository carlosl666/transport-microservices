package com.transport.order.exception;

import com.transport.order.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
                                                                    HttpStatusCode status, WebRequest request) {
        log.info("Inconveniente al mapear el data request por NoResourceFoundException, estructura json incorrecta: {}", ex.getMessage());
        return handleExceptionInternal(ex, build(null, status.value(), request.getContextPath(), ex.getMessage()), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Inconveniente al mapear el data request por MethodArgumentNotValidException, estructura json incorrecta: {}", ex.getMessage());
        return handleExceptionInternal(ex, build(null, status.value(), request.getContextPath(), ex.getMessage()), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Inconveniente al mapear el data request por HttpRequestMethodNotSupportedException, estructura json incorrecta: {}", ex.getMessage());
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return handleExceptionInternal(ex, build(null, status.value(), path, ex.getMessage()), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Inconveniente al mapear el data request por HttpMessageNotReadableException, estructura json incorrecta: {}", ex.getMessage());
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        String msg = "El cuerpo de la petición (body) es obligatorio o tiene un formato JSON incorrecto.";
        return handleExceptionInternal(ex, build(null, status.value(), path, msg), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Inconveniente al mapear el data request por TypeMismatchException, estructura json incorrecta: {}", ex.getMessage());
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        String msg = String.format("El valor %s no es válido para el campo %s.",
                ex.getValue(), ex.getPropertyName());
        return handleExceptionInternal(ex, build(null, status.value(), path, msg), headers, status, request);
    }

    @ExceptionHandler(MicroResponseException.class)
    public ResponseEntity<Object> genericExceptionHandler(MicroResponseException ex, HttpServletRequest servletRequest) {
        log.info("Inconveniente al procesar la petición: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(build(ex.getBindingResult(), ex.getStatus().value(),
                servletRequest.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus
    public ErrorResponse responseException(SQLException e, HttpServletRequest servletRequest) {
        log.info("Inconveniente SQLException message: {} INT {} {}", e.getSQLState(), e.getErrorCode(), e.getMessage());
        log.error("Inconveniente al procesar solicitud en base de datos: {}", msgError(e));
        return build(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), servletRequest.getRequestURI(), e.getMessage());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus
    public ErrorResponse responseException(InvalidDataAccessApiUsageException e, HttpServletRequest servletRequest) {
        log.error("Inconveniente al procesar solicitud por InvalidDataAccessApiUsageException, {}", msgError(e));
        return build(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), servletRequest.getRequestURI(), e.getMessage());
    }

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    @ResponseStatus
    public ErrorResponse responseException(InvalidDataAccessResourceUsageException e, HttpServletRequest servletRequest) {
        log.error("Inconveniente al procesar solicitud por InvalidDataAccessResourceUsageExceptionm, {}", msgError(e));
        return build(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), servletRequest.getRequestURI(), e.getMessage());
    }

    private ErrorResponse build(BindingResult result, int status, String path, String message) {
        List<String> details;
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now(ZoneId.systemDefault()));
        errorResponse.setStatusCode(status);
        errorResponse.setPath(path);
        if (result != null) {
            details = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
        } else {
            details = new ArrayList<>();
            details.add(message);
        }
        errorResponse.setDetalles(details);
        return errorResponse;
    }

    public static String msgError(Exception e) {
        if (e == null) {
            return "No existe informacion";
        }
        final StringBuilder builder = new StringBuilder();
        builder.append("\nTipo de Excepcion: ").append(e.getClass().getName());
        builder.append("\nFecha: ").append((LocalDateTime.now(ZoneId.systemDefault())));
        builder.append("\nMensaje: ").append((e.getMessage() == null) ? "" : e.getMessage());

        for (int i = 0; i < e.getStackTrace().length; i++) {
            final StackTraceElement stackTraceElement;
            stackTraceElement = e.getStackTrace()[i];
            builder.append("\nLocalizacion[").append(i + 1).append("]: ").append(stackTraceElement.getClassName());
            builder.append(".").append(stackTraceElement.getMethodName());
            builder.append("(").append(stackTraceElement.getFileName()).append(")");
            builder.append(":").append(stackTraceElement.getLineNumber());
        }
        if (e.getCause() != null) {
            builder.append("\n\nCausado por: ").append(e.getCause());
            if (e.getCause().getStackTrace() != null) {
                for (int i = 0; i < e.getCause().getStackTrace().length; i++) {
                    final StackTraceElement stackTraceElement;
                    stackTraceElement = e.getCause().getStackTrace()[i];
                    builder.append("\nLocalizacion[").append(i + 1).append("]: ").append(stackTraceElement.getClassName());
                    builder.append(".").append(stackTraceElement.getMethodName());
                    builder.append("(").append(stackTraceElement.getFileName()).append(")");
                    builder.append(":").append(stackTraceElement.getLineNumber());
                }
            }
        }
        return builder.toString();
    }
}
