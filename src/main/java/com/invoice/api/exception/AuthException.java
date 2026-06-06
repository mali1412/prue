package com.invoice.api.exception;

public class AuthException extends RuntimeException {
    private int httpStatus;

    public AuthException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
