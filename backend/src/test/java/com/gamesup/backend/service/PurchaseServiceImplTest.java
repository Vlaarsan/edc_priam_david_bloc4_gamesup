package com.gamesup.backend.service;

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
import com.gamesup.backend.service.impl.PurchaseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private PurchaseLineRepository purchaseLineRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @Test
    void create_shouldSavePurchaseAndLines() {
        User user = new User(); user.setId(1L); user.setUsername("alice");
        Game game = new Game(); game.setId(1L); game.setNom("Catan"); game.setPrix(45.0);
        Purchase saved = new Purchase(); saved.setId(1L); saved.setUser(user);
        saved.setPurchaseDate(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(saved);

        PurchaseDTO result = purchaseService.create(1L, List.of(1L));

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getLines()).hasSize(1);
        verify(purchaseRepository).save(any(Purchase.class));
        verify(purchaseLineRepository).saveAll(anyList());
    }

    @Test
    void getByUserId_shouldReturnFilteredList() {
        Purchase p = new Purchase(); p.setId(1L);
        when(purchaseRepository.findByUserId(1L)).thenReturn(List.of(p));

        List<PurchaseDTO> result = purchaseService.getByUserId(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void delete_shouldReturnTrueWhenDeleted() {
        when(purchaseRepository.existsById(1L)).thenReturn(true);
        boolean result = purchaseService.delete(1L);
        assertThat(result).isTrue();
        verify(purchaseRepository).deleteById(1L);
    }
}