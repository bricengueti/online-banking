package com.onlinebank.authService.controller;

import com.onlinebank.common.dto.request.Login;
import com.onlinebank.common.dto.request.Register;
import com.onlinebank.common.dto.response.ApiResponse;
import com.onlinebank.common.dto.response.AuthResponse;
import com.onlinebank.common.dto.response.UserDTO;
import com.onlinebank.common.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.onlinebank.authService.entity.User;
import com.onlinebank.authService.service.AuthService;

@RestController
@RequestMapping("")
@Tag(name = "Authentification", description = "Endpoints pour l'inscription, la connexion et la gestion des utilisateurs")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @Operation(
            summary = "Inscription utilisateur",
            description = "Crée un nouvel utilisateur avec rôle par défaut USER."
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody Register register) {
        User user = authService.register(register);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(user.getEmail(), "User registered successfully"));
    }

    @Operation(
            summary = "Connexion utilisateur",
            description = "Authentifie l'utilisateur et retourne un token JWT."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody Login login) {
        AuthResponse response = authService.login(login);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @Operation(
            summary = "Utilisateur courant",
            description = "Retourne les informations de l'utilisateur authentifié. Nécessite un token JWT valide."
    )
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser(@RequestHeader("X-Auth-Token") String token) {
        String email = jwtUtils.extractUsername(token);
        User user = authService.getCurrentUser(email);
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
        return ResponseEntity.ok(ApiResponse.success(userDTO, "User retrieved successfully"));
    }

}