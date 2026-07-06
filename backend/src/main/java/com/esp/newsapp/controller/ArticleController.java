package com.esp.newsapp.controller;

import com.esp.newsapp.dto.ArticleDTO;
import com.esp.newsapp.dto.CategoryWithArticlesDTO;
import com.esp.newsapp.service.ArticleService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Service REST public : articles au format JSON ou XML selon le parametre "format"
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public List<ArticleDTO> getArticles(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size) {
        return articleService.getArticles(page, size);
    }

    @GetMapping(value = "/count")
    public Map<String, Long> count() {
        return Map.of("total", articleService.countArticles());
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ArticleDTO getById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @GetMapping(value = "/categorie/{categoryId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public List<ArticleDTO> getByCategory(@PathVariable Long categoryId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        return articleService.getArticlesByCategory(categoryId, page, size);
    }

    @GetMapping(value = "/groupes-par-categorie", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public List<CategoryWithArticlesDTO> getGroupedByCategory() {
        return articleService.getArticlesGroupedByCategory();
    }
}
