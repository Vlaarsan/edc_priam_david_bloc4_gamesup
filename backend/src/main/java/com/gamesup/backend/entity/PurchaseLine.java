package com.gamesup.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "purchase_lines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(nullable = false)
    private Double prix;

    @Column(nullable = false)
    private Integer quantite;
}
