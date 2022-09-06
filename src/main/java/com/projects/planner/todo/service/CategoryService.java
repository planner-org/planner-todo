package com.projects.planner.todo.service;

import com.projects.planner.entity.Category;
import com.projects.planner.todo.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category with id=" + id + " not found!"));
    }

    public List<Category> findAll(Long userId) {
        return categoryRepository.findByUserIdOrderByTitleAsc(userId);
    }

    public List<Category> findByTitle(String title, Long userId) {
        return categoryRepository.findByTitle(title, userId);
    }

    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Category category) {
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
