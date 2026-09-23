package com.example.scaffold.common.api;

import org.springframework.http.HttpStatus;

public enum ResultCode {

    SUCCESS(0, "success", HttpStatus.OK),
    BAD_REQUEST(400, "bad request", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(400, "validation error", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "resource not found", HttpStatus.NOT_FOUND),
    CONFLICT(409, "resource conflict", HttpStatus.CONFLICT),
    INTERNAL_ERROR(500, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus status;

    ResultCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public HttpStatus status() {
        return status;
    }
}
