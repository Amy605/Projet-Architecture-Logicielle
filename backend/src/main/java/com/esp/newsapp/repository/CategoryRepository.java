package com.esp.newsapp.repository;

import com.esp.newsapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNom(String nom);
}
