package com.onlinebank.common.config;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware to get the current user from Spring Security context.
 * Used by Spring Data MongoDB for automatic auditing.
 */
@Component
@Primary
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("system");
            }

            String username = authentication.getName();
            return Optional.of(username != null ? username : "system");

        } catch (Exception e) {
            // Fallback if security context is not available (e.g., during startup)
            return Optional.of("system");
        }
    }
}