package com.esp.newsapp.dto;

import java.util.List;

public record CategoryWithArticlesDTO(Long id, String nom, List<ArticleDTO> articles) {}
