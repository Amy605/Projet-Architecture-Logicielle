package com.esp.newsapp.service;

import com.esp.newsapp.dto.ArticleDTO;
import com.esp.newsapp.dto.ArticleSaveDTO;
import com.esp.newsapp.dto.CategoryWithArticlesDTO;
import com.esp.newsapp.model.Article;
import com.esp.newsapp.model.Category;
import com.esp.newsapp.repository.ArticleRepository;
import com.esp.newsapp.repository.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    public ArticleService(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ArticleDTO> getArticles(int page, int size) {
        return articleRepository.findAllByOrderByDatePublicationDesc(PageRequest.of(page, size))
                .map(this::toDTO).getContent();
    }

    public long countArticles() {
        return articleRepository.count();
    }

    public ArticleDTO getArticleById(Long id) {
        Article a = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article introuvable : " + id));
        return toDTO(a);
    }

    public List<ArticleDTO> getArticlesByCategory(Long categoryId, int page, int size) {
        Category c = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Categorie introuvable : " + categoryId));
        return articleRepository.findByCategorieOrderByDatePublicationDesc(c, PageRequest.of(page, size))
                .map(this::toDTO).getContent();
    }

    public List<CategoryWithArticlesDTO> getArticlesGroupedByCategory() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryWithArticlesDTO(
                        c.getId(), c.getNom(),
                        articleRepository.findByCategorieOrderByDatePublicationDesc(c, PageRequest.of(0, 1000))
                                .map(this::toDTO).getContent()))
                .toList();
    }

    public ArticleDTO createArticle(ArticleSaveDTO dto) {
        Category cat = categoryRepository.findById(dto.categorieId())
                .orElseThrow(() -> new IllegalArgumentException("Categorie introuvable"));
        Article a = Article.builder()
                .titre(dto.titre()).resume(dto.resume()).contenu(dto.contenu()).categorie(cat)
                .build();
        return toDTO(articleRepository.save(a));
    }

    public ArticleDTO updateArticle(Long id, ArticleSaveDTO dto) {
        Article a = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article introuvable"));
        Category cat = categoryRepository.findById(dto.categorieId())
                .orElseThrow(() -> new IllegalArgumentException("Categorie introuvable"));
        a.setTitre(dto.titre());
        a.setResume(dto.resume());
        a.setContenu(dto.contenu());
        a.setCategorie(cat);
        return toDTO(articleRepository.save(a));
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    private ArticleDTO toDTO(Article a) {
        return new ArticleDTO(a.getId(), a.getTitre(), a.getResume(), a.getContenu(),
                a.getDatePublication(), a.getCategorie().getNom());
    }
}
