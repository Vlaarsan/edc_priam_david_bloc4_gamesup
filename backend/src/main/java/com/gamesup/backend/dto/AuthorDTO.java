package com.gamesup.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO {

    private Long id;
    private String name;
}
