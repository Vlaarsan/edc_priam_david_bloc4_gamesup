package com.gamesup.backend.service;

import com.gamesup.backend.dto.UserDTO;
import com.gamesup.backend.dto.UserRequestDTO;
import com.gamesup.backend.entity.User;
import com.gamesup.backend.repository.UserRepository;
import com.gamesup.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void register_shouldSaveUserAndReturnDto() {
        UserRequestDTO request = new UserRequestDTO("alice", "alice@test.com", "password", null);
        User saved = new User();
        saved.setId(1L);
        saved.setUsername("alice");
        saved.setEmail("alice@test.com");
        saved.setRole(User.Role.ROLE_CLIENT);

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserDTO result = userService.register(request);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("alice");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowWhenUsernameExists() {
        UserRequestDTO request = new UserRequestDTO("alice", "alice@test.com", "password", null);
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username already exists");
    }

    @Test
    void getAllUsers_shouldReturnList() {
        User u1 = new User(); u1.setId(1L); u1.setUsername("alice"); u1.setEmail("a@test.com");
        User u2 = new User(); u2.setId(2L); u2.setUsername("bob"); u2.setEmail("b@test.com");
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UserDTO> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_shouldReturnDtoWhenFound() {
        User u = new User(); u.setId(1L); u.setUsername("alice"); u1.setEmail("a@test.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        Optional<UserDTO> result = userService.getUserById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("alice");
    }

    @Test
    void deleteUser_shouldReturnTrueWhenDeleted() {
        when(userRepository.existsById(1L)).thenReturn(true);
        boolean result = userService.deleteUser(1L);
        assertThat(result).isTrue();
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_shouldReturnFalseWhenNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);
        boolean result = userService.deleteUser(99L);
        assertThat(result).isFalse();
    }
}