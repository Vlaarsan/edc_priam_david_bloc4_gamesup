package com.gamesup.backend.service.impl;

import com.gamesup.backend.dto.CategoryDTO;
import com.gamesup.backend.entity.Category;
import com.gamesup.backend.repository.CategoryRepository;
import com.gamesup.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDTO create(String type) {
        Category category = Category.builder().type(type).build();
        Category saved = categoryRepository.save(category);
        return toDto(saved);
    }

    @Override
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDTO> getById(Long id) {
        return categoryRepository.findById(id).map(this::toDto);
    }

    @Override
    public boolean delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            return false;
        }
        categoryRepository.deleteById(id);
        return true;
    }

    private CategoryDTO toDto(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .type(category.getType())
                .build();
    }
}
