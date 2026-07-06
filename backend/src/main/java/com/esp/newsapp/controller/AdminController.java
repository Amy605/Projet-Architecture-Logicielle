package com.esp.newsapp.controller;

import com.esp.newsapp.dto.TokenDTO;
import com.esp.newsapp.dto.UserDTO;
import com.esp.newsapp.dto.UserSaveDTO;
import com.esp.newsapp.service.AuthTokenService;
import com.esp.newsapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Reserve a l'ADMIN : gestion des utilisateurs et des jetons d'authentification SOAP
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final AuthTokenService authTokenService;

    public AdminController(UserService userService, AuthTokenService authTokenService) {
        this.userService = userService;
        this.authTokenService = authTokenService;
    }

    @GetMapping("/utilisateurs")
    public List<UserDTO> getUsers() {
        return userService.getAll();
    }

    @PostMapping("/utilisateurs")
    public UserDTO createUser(@RequestBody UserSaveDTO dto) {
        return userService.create(dto);
    }

    @PutMapping("/utilisateurs/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserSaveDTO dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/utilisateurs/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/jetons")
    public List<TokenDTO> getTokens() {
        return authTokenService.getAll();
    }

    @PostMapping("/jetons")
    public TokenDTO createToken(@RequestBody Map<String, String> body) {
        return authTokenService.generate(body.getOrDefault("description", ""));
    }

    @DeleteMapping("/jetons/{id}")
    public void deleteToken(@PathVariable Long id) {
        authTokenService.delete(id);
    }
}
