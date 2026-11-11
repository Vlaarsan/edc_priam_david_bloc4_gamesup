package com.gamesup.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseLineDTO {

    private Long id;
    private Long gameId;
    private Double prix;
    private Integer quantite;
}
