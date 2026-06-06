package com.invoice.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoice.api.service.SvcAuth;
import com.invoice.api.exception.AuthException;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/auth")
public class CtrlAuth {

    @Autowired
    private SvcAuth svc;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        // Validar datos de entrada
        if (body.get("username") == null || body.get("username").trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Username es requerido"));
        }
        if (body.get("password") == null || body.get("password").trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Password es requerido"));
        }
        if (body.get("role") == null || body.get("role").trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Role es requerido"));
        }

        try {
            String result = svc.register(
                body.get("username"),
                body.get("password"),
                body.get("role")
            );
            return ResponseEntity.ok(Map.of("message", result));
        } catch (AuthException e) {
            return ResponseEntity.status(e.getHttpStatus())
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        // Validar datos de entrada
        if (body.get("username") == null || body.get("username").trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Username es requerido"));
        }
        if (body.get("password") == null || body.get("password").trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Password es requerido"));
        }

        try {
            String token = svc.login(body.get("username"), body.get("password"));
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthException e) {
            return ResponseEntity.status(e.getHttpStatus())
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }
}
