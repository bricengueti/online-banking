package com.onlinebank.common.util;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT authentication filter for validating tokens on each request.
 * Extracts JWT from request header, validates it, and sets authentication in context.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    // CORRECTION: Changé JwtFilter.class en AuthTokenFilter.class
    private static final Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);

    private final JwtUtils jwtUtils;

    public AuthTokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();


        try {
            String jwt = jwtUtils.parseJwt(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Extract user information from JWT claims
                String username = jwtUtils.extractUsername(jwt);

                // Extract authorities/roles from claims (supports multiple roles)
                Set<SimpleGrantedAuthority> authorities = extractAuthorities(jwt);

                // Create authentication token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("User '{}' authenticated successfully with authorities: {} - URI: {}",
                        username,
                        authorities.stream().map(a -> a.getAuthority()).collect(Collectors.toList()),
                        requestURI
                );
            } else if (jwt != null) {
                log.warn("Invalid JWT token for request: {}", requestURI);
            }

        } catch (Exception e) {
            log.error("Cannot set user authentication for URI {}: {}", requestURI, e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract authorities/roles from JWT claims
     * Supports multiple formats: single role, list of roles, or authorities
     */
    @SuppressWarnings("unchecked")
    private Set<SimpleGrantedAuthority> extractAuthorities(String token) {
        try {
            // Try to extract authorities as list
            List<String> roles = jwtUtils.extractClaim(token, claims -> {
                // Check for different claim names
                if (claims.get("authorities") != null) {
                    return claims.get("authorities", List.class);
                } else if (claims.get("roles") != null) {
                    return claims.get("roles", List.class);
                } else if (claims.get("role") != null) {
                    return List.of(claims.get("role", String.class));
                } else {
                    return List.of("ROLE_USER"); // Default role
                }
            });

            if (roles != null && !roles.isEmpty()) {
                return roles.stream()
                        .map(role -> {
                            // Ensure role has ROLE_ prefix
                            String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                            return new SimpleGrantedAuthority(formattedRole);
                        })
                        .collect(Collectors.toSet());
            }
        } catch (Exception e) {
            log.debug("Could not extract authorities from token: {}", e.getMessage());
        }

        // Default authority if none found
        return Set.of(new SimpleGrantedAuthority("ROLE_USER"));
    }


    /**
     * Check if request should be filtered based on path
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Skip filtering for OPTIONS requests (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        return super.shouldNotFilter(request);
    }
}