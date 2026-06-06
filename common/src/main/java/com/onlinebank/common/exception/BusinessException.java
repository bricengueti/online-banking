package com.onlinebank.common.config.exception;

import org.springframework.http.HttpStatus;

/**
 * Base business exception for banking operations.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;
    private final String details;

    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = "BUSINESS_ERROR";
        this.details = null;
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = "BUSINESS_ERROR";
        this.details = null;
    }

    public BusinessException(String message, String errorCode) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorCode;
        this.details = null;
    }

    public BusinessException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.details = null;
    }

    public BusinessException(String message, String errorCode, HttpStatus status, String details) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
        this.details = details;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = "BUSINESS_ERROR";
        this.details = null;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDetails() {
        return details;
    }
}