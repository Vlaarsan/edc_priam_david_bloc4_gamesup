package com.gamesup.backend.service;

import com.gamesup.backend.dto.GameDTO;
import com.gamesup.backend.dto.GameRequestDTO;

import java.util.List;
import java.util.Optional;

public interface GameService {

    GameDTO create(GameRequestDTO request);

    List<GameDTO> getAll();

    Optional<GameDTO> getById(Long id);

    List<GameDTO> searchByNom(String nom);

    GameDTO update(Long id, GameRequestDTO request);

    boolean delete(Long id);
}
