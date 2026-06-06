package com.onlinebank.common.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication response DTO with JWT token.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
public record AuthResponse(
        @JsonProperty("token")
        String token,

        @JsonProperty("type")
        String type,

        @JsonProperty("username")
        String username,

        @JsonProperty("expires_in")
        Long expiresIn
) {
    // Default factory method
    public static AuthResponse of(String token, String username, Long expiresIn) {
        return new AuthResponse(token, "Bearer", username, expiresIn);
    }

    // Convenience method
    public static AuthResponse fromToken(String token, String username) {
        return new AuthResponse(token, "Bearer", username, 3600L); // 1 hour default
    }

    public String getFullToken() {
        return type + " " + token;
    }
}