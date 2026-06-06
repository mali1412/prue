package com.invoice.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.invoice.api.exception.AuthException;
import com.invoice.config.jwt.JwtUtil;
import com.invoice.api.model.User;
import com.invoice.api.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SvcAuth {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(String username, String password, String role) {
        // Validar que el usuario no exista
        if (userRepository.findByUsername(username).isPresent()) {
            throw new AuthException("El usuario ya existe", HttpStatus.CONFLICT.value());
        }

        // Crear nuevo usuario
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRole(role);

        userRepository.save(user);
        return "Usuario registrado exitosamente";
    }

    public String login(String username, String password) {
        // Buscar el usuario
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthException("Usuario no encontrado", HttpStatus.UNAUTHORIZED.value()));

        // Validar contraseña
        if (!encoder.matches(password, user.getPassword())) {
            throw new AuthException("Contraseña incorrecta", HttpStatus.UNAUTHORIZED.value());
        }

        // Generar token JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("roles", List.of(Map.of("authority", user.getRole())));

        return jwtUtil.generateToken(username, claims);
    }
}
