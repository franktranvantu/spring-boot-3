package com.franktranvantu.springboot3.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ServiceStatusCode {
    SUCCESS(2000, HttpStatus.OK),
    UNEXPECTED_ERROR(9999, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error"),
    INVALID_TOKEN(9998, HttpStatus.INTERNAL_SERVER_ERROR, "The token is invalid"),

    UNAUTHENTICATED(4001, HttpStatus.UNAUTHORIZED, "You are not authenticated"),
    UNAUTHORIZED(4003, HttpStatus.FORBIDDEN, "You do not have permission"),

    USER_INVALID_REQUEST(4001, HttpStatus.BAD_REQUEST),
    USER_EXISTED(4002, HttpStatus.BAD_REQUEST, "The user already existed"),
    USER_NOT_FOUND(4003, HttpStatus.NOT_FOUND, "The user not found"),

    ROLE_EXISTED(4011, HttpStatus.BAD_REQUEST, "The role already existed"),
    ROLE_NOT_FOUND(4012, HttpStatus.NOT_FOUND, "The role not found"),

    PERMISSION_EXISTED(4021, HttpStatus.BAD_REQUEST, "The permission already existed"),
    PERMISSION_NOT_FOUND(4022, HttpStatus.NOT_FOUND, "The permission not found"),
    ;

    final int code;
    final HttpStatusCode statusCode;
    String message;
}
