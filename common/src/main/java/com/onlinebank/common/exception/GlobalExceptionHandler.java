package com.onlinebank.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(com.onlinebank.common.config.exception.BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            com.onlinebank.common.config.exception.BusinessException ex,
            WebRequest request) {

        log.error("BusinessException: {} - {}", ex.getErrorCode(), ex.getMessage(), ex);

        ErrorResponse error = ErrorResponse.builder()
                .status(ex.getStatus().value())
                .error(ex.getStatus().getReasonPhrase())
                .message(ex.getMessage())
                .errorCode(ex.getErrorCode())
                .path(getPath(request))
                .build();

        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        log.warn("ValidationException: ", ex);

        FieldError firstError = ex.getBindingResult().getFieldErrors().get(0);

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation error on field: " + firstError.getField())
                .errorCode("VAL_001")
                .path(getPath(request))
                .validationError(new ErrorResponse.ValidationError(
                        firstError.getField(),
                        String.valueOf(firstError.getRejectedValue()),
                        firstError.getDefaultMessage()
                ))
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            WebRequest request) {

        log.error("Unhandled exception: ", ex);

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An internal error occurred: " + ex.getMessage())
                .errorCode("SYS_001")
                .path(getPath(request))
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getPath(WebRequest request) {
        String description = request.getDescription(false);
        return description != null ? description.replace("uri=", "") : "unknown";
    }
}