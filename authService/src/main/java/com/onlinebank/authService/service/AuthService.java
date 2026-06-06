package com.onlinebank.authService.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.onlinebank.common.dto.request.Login;
import com.onlinebank.common.dto.request.Register;
import com.onlinebank.common.dto.response.AuthResponse;
import com.onlinebank.common.exception.DuplicateResourceException;
import com.onlinebank.common.exception.ResourceNotFoundException;
import com.onlinebank.common.util.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onlinebank.authService.entity.User;
import com.onlinebank.authService.repository.UserRepository;


/**
 * Authentication service for user registration and login.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Register a new user
     *
     * @param register the registration request
     * @return the created user
     */
    public User register(Register register) {
        // Check if email already exists
        if (userRepository.existsByEmail(register.email())) {
            throw new DuplicateResourceException("User", "email", register.email());
        }

        // Create new user
        User user = new User();
        user.setName(register.name());
        user.setEmail(register.email());
        user.setPassword(passwordEncoder.encode(register.password()));

        return userRepository.save(user);
    }

    /**
     * Authenticate user and generate JWT token
     *
     * @param login the login request
     * @return AuthResponse with JWT token
     */
    public AuthResponse login(Login login) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.email(),
                        login.password()
                )
        );

        // Set authentication in context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Récupérer l'utilisateur
        User user = userRepository.findByEmail(login.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + login.email()));

        // Construire un Map avec les infos
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("id", user.getId());

        // Générer le JWT avec les claims
        String jwt = jwtUtils.generateToken(claims, user.getEmail());

        // Retourner la réponse
        return new AuthResponse(jwt, "Bearer", user.getEmail(), jwtUtils.getExpirationTime());
    }


    /**
     * Get current user by email
     *
     * @param email the user email
     * @return the user
     */
    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    /**
     * Validate user credentials
     *
     * @param email the user email
     * @param password the raw password
     * @return true if credentials are valid
     */
    public boolean validateCredentials(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }
}