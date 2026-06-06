package com.onlinebank.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for JWT token operations.
 * Handles generation, validation, and extraction of JWT tokens.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration:3600000}")
    private long expirationTime; // en ms (défaut: 1h = 3600000)

    @Value("${app.jwt.refresh-expiration:604800000}")
    private long refreshExpirationTime; // en ms (défaut: 7 jours = 604800000)

    @Value("${app.jwt.header:Authorization}")
    private String tokenHeader;

    @Value("${app.jwt.prefix:Bearer }")
    private String tokenPrefix;

    /**
     * Generate JWT token from Authentication object
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities());
        claims.put("type", "access");
        return generateToken(claims, userDetails.getUsername());
    }

    /**
     * Generate JWT token with custom claims
     */
    public String generateToken(Map<String, Object> claims, String username) {
        return createToken(claims, username, expirationTime);
    }

    /**
     * Generate refresh token (longer validity)
     */
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, username, refreshExpirationTime);
    }

    /**
     * Generate refresh token with custom claims
     */
    public String generateRefreshToken(Map<String, Object> claims, String username) {
        claims.put("type", "refresh");
        return createToken(claims, username, refreshExpirationTime);
    }

    /**
     * Create JWT token with specified claims and validity
     */
    private String createToken(Map<String, Object> claims, String subject, long validity) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity);

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256);

        if (claims != null && !claims.isEmpty()) {
            builder.addClaims(claims);
        }

        String token = builder.compact();
        logger.debug("Generated JWT token for user: {} with expiry: {}", subject, expiryDate);
        return token;
    }

    /**
     * Get signing key from secret
     */
    private Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Parse JWT token from HTTP request header
     */
    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(tokenHeader);

        if (headerAuth != null && headerAuth.startsWith(tokenPrefix)) {
            String token = headerAuth.substring(tokenPrefix.length());
            logger.debug("JWT token extracted from request: {}", request.getRequestURI());
            return token;
        }

        logger.debug("No JWT token found in request: {}", request.getRequestURI());
        return null;
    }


    // Add this method to JwtUtils class
    public String parseJwtFromHeader(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    /**
     * Validate JWT token
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);

            boolean isExpired = isTokenExpired(token);
            if (!isExpired) {
                logger.debug("JWT token is valid");
                return true;
            } else {
                logger.warn("JWT token is expired");
                return false;
            }
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Validate token against UserDetails
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);

            if (isValid) {
                logger.debug("Token validated successfully for user: {}", username);
            } else {
                logger.warn("Token validation failed for user: {}", username);
            }

            return isValid;
        } catch (Exception e) {
            logger.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        Date expiration = extractExpirationDate(token);
        boolean isExpired = expiration.before(new Date());

        if (isExpired) {
            logger.debug("Token expired at: {}", expiration);
        }

        return isExpired;
    }

    /**
     * Extract username from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from token
     */
    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract specific claim from token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Get token expiration time in milliseconds
     */
    public long getExpirationTime() {
        return expirationTime;
    }

    /**
     * Get refresh token expiration time in milliseconds
     */
    public long getRefreshExpirationTime() {
        return refreshExpirationTime;
    }

    /**
     * Check if token is a refresh token
     */
    public boolean isRefreshToken(String token) {
        try {
            String type = extractClaim(token, claims -> claims.get("type", String.class));
            return "refresh".equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get remaining validity in milliseconds
     */
    public long getRemainingValidity(String token) {
        Date expiration = extractExpirationDate(token);
        long remaining = expiration.getTime() - System.currentTimeMillis();
        return Math.max(0, remaining);
    }
}