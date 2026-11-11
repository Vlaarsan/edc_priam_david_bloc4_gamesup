package com.gamesup.backend.service;

import com.gamesup.backend.dto.UserDTO;
import com.gamesup.backend.dto.UserRequestDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDTO register(UserRequestDTO request);

    List<UserDTO> getAllUsers();

    Optional<UserDTO> getUserById(Long id);

    Optional<UserDTO> getUserByUsername(String username);

    boolean deleteUser(Long id);
}
