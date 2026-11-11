package com.gamesup.backend.controller;

import com.gamesup.backend.dto.GameDTO;
import com.gamesup.backend.dto.GameRequestDTO;
import com.gamesup.backend.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    void getAll_shouldReturnList() throws Exception {
        when(gameService.getAll()).thenReturn(List.of(
                new GameDTO(1L, "Catan", "Desc", 45.0, 10, 1L, 1L, 1L, 1),
                new GameDTO(2L, "Carcassonne", "Desc", 35.0, 5, 1L, 1L, 1L, 2)
        ));

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Catan"))
                .andExpect(jsonPath("$[1].nom").value("Carcassonne"));
    }

    @Test
    void getById_shouldReturnGameWhenFound() throws Exception {
        when(gameService.getById(1L)).thenReturn(
                java.util.Optional.of(new GameDTO(1L, "Catan", "Desc", 45.0, 10, 1L, 1L, 1L, 1)));

        mockMvc.perform(get("/api/games/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Catan"));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        when(gameService.getById(99L)).thenReturn(java.util.Optional.empty());
        mockMvc.perform(get("/api/games/99")).andExpect(status().isNotFound());
    }

    @Test
    void search_shouldReturnFilteredList() throws Exception {
        when(gameService.searchByNom("cat")).thenReturn(List.of(
                new GameDTO(1L, "Catan", "Desc", 45.0, 10, 1L, 1L, 1L, 1)));

        mockMvc.perform(get("/api/games/search").param("nom", "cat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Catan"));
    }

    @Test
    void create_shouldReturnCreatedGame() throws Exception {
        when(gameService.create(any(GameRequestDTO.class))).thenReturn(
                new GameDTO(1L, "Catan", "Desc", 45.0, 10, 1L, 1L, 1L, 1));

        String json = "{\"nom\":\"Catan\",\"description\":\"Desc\",\"prix\":45.0,\"stock\":10}";
        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Catan"));
    }
}