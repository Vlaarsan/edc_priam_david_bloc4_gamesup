package com.gamesup.backend.controller;

import com.gamesup.backend.dto.GameDTO;
import com.gamesup.backend.dto.GameRequestDTO;
import com.gamesup.backend.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GameController {

    private final GameService gameService;

    @GetMapping
    public ResponseEntity<List<GameDTO>> getAll() {
        return ResponseEntity.ok(gameService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getById(@PathVariable Long id) {
        return gameService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<GameDTO>> search(@RequestParam String nom) {
        return ResponseEntity.ok(gameService.searchByNom(nom));
    }

    @PostMapping
    public ResponseEntity<GameDTO> create(@RequestBody GameRequestDTO request) {
        return ResponseEntity.ok(gameService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameDTO> update(@PathVariable Long id, @RequestBody GameRequestDTO request) {
        return ResponseEntity.ok(gameService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = gameService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
