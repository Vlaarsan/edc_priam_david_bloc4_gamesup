package com.gamesup.backend.service.impl;

import com.gamesup.backend.dto.AuthorDTO;
import com.gamesup.backend.entity.Author;
import com.gamesup.backend.repository.AuthorRepository;
import com.gamesup.backend.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorDTO create(String name) {
        Author author = Author.builder().name(name).build();
        Author saved = authorRepository.save(author);
        return toDto(saved);
    }

    @Override
    public List<AuthorDTO> getAll() {
        return authorRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorDTO> getById(Long id) {
        return authorRepository.findById(id).map(this::toDto);
    }

    @Override
    public boolean delete(Long id) {
        if (!authorRepository.existsById(id)) {
            return false;
        }
        authorRepository.deleteById(id);
        return true;
    }

    private AuthorDTO toDto(Author author) {
        return AuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .build();
    }
}
