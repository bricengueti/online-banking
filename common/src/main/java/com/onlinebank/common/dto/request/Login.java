package com.onlinebank.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Login request DTO.
 * Using Java Record for immutable data transfer.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
public record Login(
        @NotBlank(message = "Email is required")
        @Size(min = 3, max = 50, message = "Email must be between 3 and 50 characters")
        @JsonProperty("email")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        @JsonProperty("password")
        String password
) {
}