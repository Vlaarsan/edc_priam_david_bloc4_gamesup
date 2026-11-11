package com.gamesup.backend.controller;

import com.gamesup.backend.dto.PublisherDTO;
import com.gamesup.backend.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<List<PublisherDTO>> getAll() {
        return ResponseEntity.ok(publisherService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getById(@PathVariable Long id) {
        return publisherService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PublisherDTO> create(@RequestParam String name) {
        return ResponseEntity.ok(publisherService.create(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = publisherService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
