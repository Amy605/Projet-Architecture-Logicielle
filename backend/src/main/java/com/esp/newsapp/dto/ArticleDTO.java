package com.esp.newsapp.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.time.LocalDateTime;

public record ArticleDTO(
        Long id,
        String titre,
        String resume,
        String contenu,
        LocalDateTime datePublication,
        String categorie
) {}
