package com.gamesup.backend.service.impl;

import com.gamesup.backend.dto.GameDTO;
import com.gamesup.backend.dto.GameRequestDTO;
import com.gamesup.backend.entity.Author;
import com.gamesup.backend.entity.Category;
import com.gamesup.backend.entity.Game;
import com.gamesup.backend.entity.Publisher;
import com.gamesup.backend.repository.AuthorRepository;
import com.gamesup.backend.repository.CategoryRepository;
import com.gamesup.backend.repository.GameRepository;
import com.gamesup.backend.repository.PublisherRepository;
import com.gamesup.backend.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;

    @Override
    public GameDTO create(GameRequestDTO request) {
        Game game = new Game();
        game.setNom(request.getNom());
        game.setDescription(request.getDescription());
        game.setPrix(request.getPrix());
        game.setStock(request.getStock());
        game.setNumEdition(request.getNumEdition());

        if (request.getAuthorId() != null) {
            Author author = authorRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Author not found"));
            game.setAuthor(author);
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            game.setCategory(category);
        }
        if (request.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(request.getPublisherId())
                    .orElseThrow(() -> new RuntimeException("Publisher not found"));
            game.setPublisher(publisher);
        }

        Game saved = gameRepository.save(game);
        return toDto(saved);
    }

    @Override
    public List<GameDTO> getAll() {
        return gameRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GameDTO> getById(Long id) {
        return gameRepository.findById(id).map(this::toDto);
    }

    @Override
    public List<GameDTO> searchByNom(String nom) {
        return gameRepository.findByNomContainingIgnoreCase(nom).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GameDTO update(Long id, GameRequestDTO request) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        game.setNom(request.getNom());
        game.setDescription(request.getDescription());
        game.setPrix(request.getPrix());
        game.setStock(request.getStock());
        game.setNumEdition(request.getNumEdition());
        Game saved = gameRepository.save(game);
        return toDto(saved);
    }

    @Override
    public boolean delete(Long id) {
        if (!gameRepository.existsById(id)) {
            return false;
        }
        gameRepository.deleteById(id);
        return true;
    }

    private GameDTO toDto(Game game) {
        return GameDTO.builder()
                .id(game.getId())
                .nom(game.getNom())
                .description(game.getDescription())
                .prix(game.getPrix())
                .stock(game.getStock())
                .authorId(game.getAuthor() != null ? game.getAuthor().getId() : null)
                .categoryId(game.getCategory() != null ? game.getCategory().getId() : null)
                .publisherId(game.getPublisher() != null ? game.getPublisher().getId() : null)
                .numEdition(game.getNumEdition())
                .build();
    }
}
