package com.esp.newsapp.service;

import com.esp.newsapp.dto.UserDTO;
import com.esp.newsapp.dto.UserSaveDTO;
import com.esp.newsapp.model.Role;
import com.esp.newsapp.model.User;
import com.esp.newsapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getRole().name())).toList();
    }

    public UserDTO create(UserSaveDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur existe deja");
        }
        User u = User.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.valueOf(dto.role().toUpperCase()))
                .build();
        u = userRepository.save(u);
        return new UserDTO(u.getId(), u.getUsername(), u.getRole().name());
    }

    public UserDTO update(Long id, UserSaveDTO dto) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        u.setUsername(dto.username());
        if (dto.password() != null && !dto.password().isBlank()) {
            u.setPassword(passwordEncoder.encode(dto.password()));
        }
        u.setRole(Role.valueOf(dto.role().toUpperCase()));
        u = userRepository.save(u);
        return new UserDTO(u.getId(), u.getUsername(), u.getRole().name());
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean checkCredentials(String username, String password) {
        return userRepository.findByUsername(username)
                .map(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElse(false);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
    }
}
