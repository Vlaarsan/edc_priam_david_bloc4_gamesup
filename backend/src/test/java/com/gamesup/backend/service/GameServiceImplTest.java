package com.gamesup.backend.service;

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
import com.gamesup.backend.service.impl.GameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    void create_shouldSaveAndReturnDto() {
        GameRequestDTO request = new GameRequestDTO("Catan", "Desc", 45.0, 10, 1L, 1L, 1L, 1);

        Author author = new Author(); author.setId(1L); author.setName("Author A");
        Category category = new Category(); category.setId(1L); category.setType("Strategie");
        Publisher publisher = new Publisher(); publisher.setId(1L); publisher.setName("Pub A");
        Game saved = new Game(); saved.setId(1L); saved.setNom("Catan");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(gameRepository.save(any(Game.class))).thenReturn(saved);

        GameDTO result = gameService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Catan");
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void getAll_shouldReturnList() {
        Game g1 = new Game(); g1.setId(1L); g1.setNom("Game A");
        Game g2 = new Game(); g2.setId(2L); g2.setNom("Game B");
        when(gameRepository.findAll()).thenReturn(List.of(g1, g2));

        List<GameDTO> result = gameService.getAll();

        assertThat(result).hasSize(2);
        verify(gameRepository).findAll();
    }

    @Test
    void getById_shouldReturnDtoWhenFound() {
        Game g = new Game(); g.setId(1L); g.setNom("Game A");
        when(gameRepository.findById(1L)).thenReturn(Optional.of(g));

        Optional<GameDTO> result = gameService.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getNom()).isEqualTo("Game A");
    }

    @Test
    void searchByNom_shouldReturnFilteredList() {
        Game g = new Game(); g.setId(1L); g.setNom("Catan");
        when(gameRepository.findByNomContainingIgnoreCase("cat")).thenReturn(List.of(g));

        List<GameDTO> result = gameService.searchByNom("cat");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Catan");
    }

    @Test
    void delete_shouldReturnTrueWhenDeleted() {
        when(gameRepository.existsById(1L)).thenReturn(true);
        boolean result = gameService.delete(1L);
        assertThat(result).isTrue();
        verify(gameRepository).deleteById(1L);
    }
}