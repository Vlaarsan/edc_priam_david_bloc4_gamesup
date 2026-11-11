package com.gamesup.backend.service.impl;

import com.gamesup.backend.dto.PublisherDTO;
import com.gamesup.backend.entity.Publisher;
import com.gamesup.backend.repository.PublisherRepository;
import com.gamesup.backend.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    @Override
    public PublisherDTO create(String name) {
        Publisher publisher = Publisher.builder().name(name).build();
        Publisher saved = publisherRepository.save(publisher);
        return toDto(saved);
    }

    @Override
    public List<PublisherDTO> getAll() {
        return publisherRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PublisherDTO> getById(Long id) {
        return publisherRepository.findById(id).map(this::toDto);
    }

    @Override
    public boolean delete(Long id) {
        if (!publisherRepository.existsById(id)) {
            return false;
        }
        publisherRepository.deleteById(id);
        return true;
    }

    private PublisherDTO toDto(Publisher publisher) {
        return PublisherDTO.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }
}
