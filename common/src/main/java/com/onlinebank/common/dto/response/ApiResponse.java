package com.onlinebank.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard API response wrapper for all REST endpoints.
 * Provides a consistent response structure across all microservices.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        @JsonProperty("success")
        boolean success,

        @JsonProperty("message")
        String message,

        @JsonProperty("data")
        T data,

        @JsonProperty("timestamp")
        String timestamp,

        @JsonProperty("status_code")
        int statusCode
) {

    /**
     * Create a success response with data
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(
                true,
                message,
                data,
                java.time.LocalDateTime.now().toString(),
                200
        );
    }

    /**
     * Create a success response with data (default message)
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation completed successfully");
    }

    /**
     * Create a success response without data
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(
                true,
                message,
                null,
                java.time.LocalDateTime.now().toString(),
                200
        );
    }

    /**
     * Create an error response
     */
    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return new ApiResponse<>(
                false,
                message,
                null,
                java.time.LocalDateTime.now().toString(),
                statusCode
        );
    }

    /**
     * Create an error response with default 400 status
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(message, 400);
    }

    /**
     * Create a created response (201)
     */
    public static <T> ApiResponse<T> created(T data, String message) {
        return new ApiResponse<>(
                true,
                message,
                data,
                java.time.LocalDateTime.now().toString(),
                201
        );
    }

    /**
     * Create a not found response (404)
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(
                false,
                message,
                null,
                java.time.LocalDateTime.now().toString(),
                404
        );
    }

    /**
     * Create an unauthorized response (401)
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(
                false,
                message,
                null,
                java.time.LocalDateTime.now().toString(),
                401
        );
    }

    /**
     * Create a forbidden response (403)
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(
                false,
                message,
                null,
                java.time.LocalDateTime.now().toString(),
                403
        );
    }

    /**
     * Check if response is successful
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Check if response has data
     */
    public boolean hasData() {
        return data != null;
    }
}