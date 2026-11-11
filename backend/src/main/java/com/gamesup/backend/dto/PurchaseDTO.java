package com.gamesup.backend.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDTO {

    private Long id;
    private Long userId;
    private LocalDateTime purchaseDate;
    private Boolean paid;
    private Boolean delivered;
    private Boolean archived;
    private List<PurchaseLineDTO> lines;
}
