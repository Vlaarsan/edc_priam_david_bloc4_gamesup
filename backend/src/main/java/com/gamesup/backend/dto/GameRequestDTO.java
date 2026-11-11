package com.gamesup.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRequestDTO {

    private String nom;
    private String description;
    private Double prix;
    private Integer stock;
    private Long authorId;
    private Long categoryId;
    private Long publisherId;
    private Integer numEdition;
}
