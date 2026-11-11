package com.gamesup.backend.service;

import com.gamesup.backend.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    CategoryDTO create(String type);

    List<CategoryDTO> getAll();

    Optional<CategoryDTO> getById(Long id);

    boolean delete(Long id);
}
