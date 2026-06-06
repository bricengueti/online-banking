package com.onlinebank.common.dto.response;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * User DTO for data transfer between services.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
public record UserDTO(
        @JsonProperty("id")
        String id,

        @JsonProperty("username")
        String username,

        @JsonProperty("email")
        String email

) {
}