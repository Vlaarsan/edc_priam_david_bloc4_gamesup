package com.gamesup.backend.controller;

import com.gamesup.backend.dto.PurchaseDTO;
import com.gamesup.backend.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> getAll() {
        return ResponseEntity.ok(purchaseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getById(@PathVariable Long id) {
        return purchaseService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<List<PurchaseDTO>> getMyPurchases(@RequestParam Long userId) {
        return ResponseEntity.ok(purchaseService.getByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<PurchaseDTO> create(@RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        List<Long> gameIds = ((List<?>) body.get("gameIds")).stream()
                .map(obj -> Long.valueOf(obj.toString()))
                .toList();
        return ResponseEntity.ok(purchaseService.create(userId, gameIds));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = purchaseService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
