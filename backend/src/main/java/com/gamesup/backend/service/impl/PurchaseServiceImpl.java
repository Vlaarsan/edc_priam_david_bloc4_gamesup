package com.gamesup.backend.service.impl;

import com.gamesup.backend.dto.PurchaseDTO;
import com.gamesup.backend.dto.PurchaseLineDTO;
import com.gamesup.backend.entity.Game;
import com.gamesup.backend.entity.Purchase;
import com.gamesup.backend.entity.PurchaseLine;
import com.gamesup.backend.entity.User;
import com.gamesup.backend.repository.GameRepository;
import com.gamesup.backend.repository.PurchaseLineRepository;
import com.gamesup.backend.repository.PurchaseRepository;
import com.gamesup.backend.repository.UserRepository;
import com.gamesup.backend.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseLineRepository purchaseLineRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Override
    public PurchaseDTO create(Long userId, List<Long> gameIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Purchase purchase = Purchase.builder()
                .user(user)
                .purchaseDate(LocalDateTime.now())
                .paid(false)
                .delivered(false)
                .archived(false)
                .build();
        Purchase saved = purchaseRepository.save(purchase);

        List<PurchaseLine> lines = gameIds.stream().map(gameId -> {
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found: " + gameId));
            return PurchaseLine.builder()
                    .purchase(saved)
                    .game(game)
                    .prix(game.getPrix())
                    .quantite(1)
                    .build();
        }).collect(Collectors.toList());
        purchaseLineRepository.saveAll(lines);

        return toDto(saved);
    }

    @Override
    public List<PurchaseDTO> getAll() {
        return purchaseRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PurchaseDTO> getById(Long id) {
        return purchaseRepository.findById(id).map(this::toDto);
    }

    @Override
    public List<PurchaseDTO> getByUserId(Long userId) {
        return purchaseRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Long id) {
        if (!purchaseRepository.existsById(id)) {
            return false;
        }
        purchaseRepository.deleteById(id);
        return true;
    }

    private PurchaseDTO toDto(Purchase purchase) {
        List<PurchaseLineDTO> lineDtos = purchase.getLines() != null
                ? purchase.getLines().stream()
                .map(line -> PurchaseLineDTO.builder()
                        .id(line.getId())
                        .gameId(line.getGame().getId())
                        .prix(line.getPrix())
                        .quantite(line.getQuantite())
                        .build())
                .collect(Collectors.toList())
                : List.of();

        return PurchaseDTO.builder()
                .id(purchase.getId())
                .userId(purchase.getUser().getId())
                .purchaseDate(purchase.getPurchaseDate())
                .paid(purchase.getPaid())
                .delivered(purchase.getDelivered())
                .archived(purchase.getArchived())
                .lines(lineDtos)
                .build();
    }
}
