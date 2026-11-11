package com.gamesup.backend.service;

import com.gamesup.backend.dto.AuthorDTO;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorDTO create(String name);

    List<AuthorDTO> getAll();

    Optional<AuthorDTO> getById(Long id);

    boolean delete(Long id);
}
