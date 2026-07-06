package com.esp.newsapp.repository;

import com.esp.newsapp.model.Article;
import com.esp.newsapp.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByOrderByDatePublicationDesc(Pageable pageable);
    Page<Article> findByCategorieOrderByDatePublicationDesc(Category categorie, Pageable pageable);
}
