package com.onlinebank.common.exception;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        @JsonProperty("timestamp") String timestamp,
        @JsonProperty("status") int status,
        @JsonProperty("error") String error,
        @JsonProperty("message") String message,
        @JsonProperty("error_code") String errorCode,
        @JsonProperty("path") String path,
        @JsonProperty("validation_error") ValidationError validationError,
        @JsonProperty("validation_errors") Map<String, String> validationErrors
) {

    public static Builder builder() {
        return new Builder();
    }

    public record ValidationError(
            @JsonProperty("field") String field,
            @JsonProperty("rejected_value") String rejectedValue,
            @JsonProperty("message") String message
    ) {}

    public static class Builder {
        private String timestamp = LocalDateTime.now().toString();
        private int status;
        private String error;
        private String message;
        private String errorCode;
        private String path;
        private ValidationError validationError;
        private Map<String, String> validationErrors;

        public Builder status(int status) { this.status = status; return this; }
        public Builder error(String error) { this.error = error; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder errorCode(String errorCode) { this.errorCode = errorCode; return this; }
        public Builder path(String path) { this.path = path; return this; }
        public Builder validationError(ValidationError validationError) {
            this.validationError = validationError;
            return this;
        }
        public Builder validationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(timestamp, status, error, message, errorCode, path, validationError, validationErrors);
        }
    }
}