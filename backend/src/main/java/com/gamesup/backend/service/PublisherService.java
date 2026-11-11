package com.gamesup.backend.service;

import com.gamesup.backend.dto.PublisherDTO;

import java.util.List;
import java.util.Optional;

public interface PublisherService {

    PublisherDTO create(String name);

    List<PublisherDTO> getAll();

    Optional<PublisherDTO> getById(Long id);

    boolean delete(Long id);
}
