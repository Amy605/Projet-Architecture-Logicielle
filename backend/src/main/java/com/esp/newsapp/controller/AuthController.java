package com.esp.newsapp.controller;

import com.esp.newsapp.dto.LoginRequest;
import com.esp.newsapp.dto.LoginResponse;
import com.esp.newsapp.model.User;
import com.esp.newsapp.security.JwtService;
import com.esp.newsapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (!userService.checkCredentials(request.username(), request.password())) {
            return ResponseEntity.status(401).body("Identifiants invalides");
        }
        User u = userService.getByUsername(request.username());
        String token = jwtService.generateToken(u.getUsername(), u.getRole().name());
        return ResponseEntity.ok(new LoginResponse(token, u.getUsername(), u.getRole().name()));
    }
}
