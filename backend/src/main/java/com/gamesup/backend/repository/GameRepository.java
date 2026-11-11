package com.gamesup.backend.repository;

import com.gamesup.backend.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    List<Game> findByAuthorId(Long authorId);

    List<Game> findByCategoryId(Long categoryId);

    List<Game> findByPublisherId(Long publisherId);

    List<Game> findByNomContainingIgnoreCase(String nom);
}
