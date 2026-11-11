package com.gamesup.backend.service;

import com.gamesup.backend.dto.PurchaseDTO;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {

    PurchaseDTO create(Long userId, List<Long> gameIds);

    List<PurchaseDTO> getAll();

    Optional<PurchaseDTO> getById(Long id);

    List<PurchaseDTO> getByUserId(Long userId);

    boolean delete(Long id);
}
