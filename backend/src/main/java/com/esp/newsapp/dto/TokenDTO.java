package com.esp.newsapp.dto;

import java.time.LocalDateTime;

public record TokenDTO(Long id, String token, String description, LocalDateTime dateCreation) {}
