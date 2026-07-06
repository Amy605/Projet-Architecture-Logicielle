package com.esp.newsapp.service;

import com.esp.newsapp.dto.TokenDTO;
import com.esp.newsapp.model.AuthToken;
import com.esp.newsapp.repository.AuthTokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthTokenService {

    private final AuthTokenRepository authTokenRepository;

    public AuthTokenService(AuthTokenRepository authTokenRepository) {
        this.authTokenRepository = authTokenRepository;
    }

    public List<TokenDTO> getAll() {
        return authTokenRepository.findAll().stream()
                .map(t -> new TokenDTO(t.getId(), t.getToken(), t.getDescription(), t.getDateCreation()))
                .toList();
    }

    public TokenDTO generate(String description) {
        AuthToken t = AuthToken.builder()
                .token(UUID.randomUUID().toString())
                .description(description)
                .build();
        t = authTokenRepository.save(t);
        return new TokenDTO(t.getId(), t.getToken(), t.getDescription(), t.getDateCreation());
    }

    public void delete(Long id) {
        authTokenRepository.deleteById(id);
    }

    public boolean isValid(String token) {
        return token != null && authTokenRepository.existsByToken(token);
    }
}
