package com.gamesup.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {

    private String username;
    private String email;
    private String password;
    private UserRole role;

    public enum UserRole {
        ROLE_CLIENT, ROLE_ADMIN
    }
}
