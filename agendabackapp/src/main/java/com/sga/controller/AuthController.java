package com.sga.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sga.config.JwtUtils;
import com.sga.dto.ErrorResponse;
import com.sga.dto.JwtResponse;
import com.sga.dto.LoginRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authManager, JwtUtils jwtUtils) {
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest req) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );

            // Obtenemos el usuario autenticado
            org.springframework.security.core.userdetails.User userSpring =
                    (org.springframework.security.core.userdetails.User) auth.getPrincipal();

            // Generamos token JWT
            String jwt = jwtUtils.generateToken(userSpring.getUsername());

            // Retornamos token con tipo Bearer
            return ResponseEntity.ok(new JwtResponse(jwt, "Bearer"));

        } catch (AuthenticationException ex) {
            // Retornamos error de autenticación con mensaje estandarizado
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciales inválidas"));
        }
    }
}
