package com.gamesup.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private UserRole role;

    public enum UserRole {
        ROLE_CLIENT, ROLE_ADMIN
    }
}
