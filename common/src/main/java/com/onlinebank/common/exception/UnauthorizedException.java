package com.onlinebank.common.exception;

import org.springframework.http.HttpStatus;

import com.onlinebank.common.config.exception.BusinessException;
/**
 * Exception thrown for authentication/authorization failures.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
public class UnauthorizedException extends BusinessException {

    private static final String ERROR_CODE = "UNAUTHORIZED";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException(String message) {
        super(message, ERROR_CODE, STATUS);
    }

    public UnauthorizedException() {
        super("Authentication required", ERROR_CODE, STATUS);
    }

    public UnauthorizedException(String message, String details) {
        super(message, ERROR_CODE, STATUS, details);
    }

    public UnauthorizedException.InvalidTokenException invalidToken() {
        return new InvalidTokenException();
    }

    public UnauthorizedException.ExpiredTokenException expiredToken() {
        return new ExpiredTokenException();
    }

    // Nested specific exceptions
    public static class InvalidTokenException extends UnauthorizedException {
        public InvalidTokenException() {
            super("Invalid or malformed JWT token", "TOKEN_INVALID");
        }
    }

    public static class ExpiredTokenException extends UnauthorizedException {
        public ExpiredTokenException() {
            super("JWT token has expired", "TOKEN_EXPIRED");
        }
    }
}