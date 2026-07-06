package com.esp.newsapp.service;

import com.esp.newsapp.dto.CategoryDTO;
import com.esp.newsapp.model.Category;
import com.esp.newsapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryDTO(c.getId(), c.getNom())).toList();
    }

    public CategoryDTO create(String nom) {
        Category c = categoryRepository.save(Category.builder().nom(nom).build());
        return new CategoryDTO(c.getId(), c.getNom());
    }

    public CategoryDTO update(Long id, String nom) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categorie introuvable"));
        c.setNom(nom);
        return new CategoryDTO(c.getId(), categoryRepository.save(c).getNom());
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
