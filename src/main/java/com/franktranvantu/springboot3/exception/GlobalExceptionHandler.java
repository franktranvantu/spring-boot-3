package com.franktranvantu.springboot3.exception;

import static com.franktranvantu.springboot3.exception.ServiceStatusCode.UNAUTHORIZED;
import static com.franktranvantu.springboot3.exception.ServiceStatusCode.UNEXPECTED_ERROR;
import static com.franktranvantu.springboot3.exception.ServiceStatusCode.USER_INVALID_REQUEST;

import com.franktranvantu.springboot3.dto.response.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ServiceResponse> handlingCommonException(Exception exception) {
        final var serviceResponse = ServiceResponse.builder()
                .code(UNEXPECTED_ERROR.getCode())
                .message(UNEXPECTED_ERROR.getMessage())
                .build();
        return ResponseEntity.internalServerError().body(serviceResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ServiceResponse> handlingServiceException(ServiceException exception) {
        final var serviceResponse = ServiceResponse.builder()
                .code(exception.getServiceStatusCode().getCode())
                .message(exception.getServiceStatusCode().getMessage())
                .build();
        return ResponseEntity.status(exception.getServiceStatusCode().getStatusCode())
                .body(serviceResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ServiceResponse> handlingMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        final var serviceResponse = ServiceResponse.builder()
                .code(USER_INVALID_REQUEST.getCode())
                .message(exception.getFieldError().getDefaultMessage())
                .build();
        return ResponseEntity.badRequest().body(serviceResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ServiceResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        final var errorCode = UNAUTHORIZED;
        final var serviceResponse = ServiceResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(serviceResponse);
    }
}
