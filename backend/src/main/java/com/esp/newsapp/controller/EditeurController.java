package com.esp.newsapp.controller;

import com.esp.newsapp.dto.ArticleDTO;
import com.esp.newsapp.dto.ArticleSaveDTO;
import com.esp.newsapp.dto.CategoryDTO;
import com.esp.newsapp.service.ArticleService;
import com.esp.newsapp.service.CategoryService;
import org.springframework.web.bind.annotation.*;

// Reserve aux EDITEUR et ADMIN : gestion des articles et categories
@RestController
@RequestMapping("/api/editeur")
public class EditeurController {

    private final ArticleService articleService;
    private final CategoryService categoryService;

    public EditeurController(ArticleService articleService, CategoryService categoryService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
    }

    @PostMapping("/articles")
    public ArticleDTO createArticle(@RequestBody ArticleSaveDTO dto) {
        return articleService.createArticle(dto);
    }

    @PutMapping("/articles/{id}")
    public ArticleDTO updateArticle(@PathVariable Long id, @RequestBody ArticleSaveDTO dto) {
        return articleService.updateArticle(id, dto);
    }

    @DeleteMapping("/articles/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }

    @PostMapping("/categories")
    public CategoryDTO createCategory(@RequestBody CategoryDTO dto) {
        return categoryService.create(dto.nom());
    }

    @PutMapping("/categories/{id}")
    public CategoryDTO updateCategory(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        return categoryService.update(id, dto.nom());
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
