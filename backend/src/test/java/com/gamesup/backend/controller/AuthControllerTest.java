package com.gamesup.backend.controller;

import com.gamesup.backend.dto.AuthRequestDTO;
import com.gamesup.backend.dto.UserDTO;
import com.gamesup.backend.dto.UserRequestDTO;
import com.gamesup.backend.security.JwtService;
import com.gamesup.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @Test
    void login_shouldReturnToken() throws Exception {
        when(jwtService.generateToken(any())).thenReturn("fake-token");

        String json = "{\"username\":\"alice\",\"password\":\"password\"}";
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-token"));
    }

    @Test
    void register_shouldReturnUser() throws Exception {
        UserDTO dto = new UserDTO(1L, "alice", "alice@test.com", UserDTO.UserRole.ROLE_CLIENT);
        when(userService.register(any(UserRequestDTO.class))).thenReturn(dto);

        String json = "{\"username\":\"alice\",\"email\":\"alice@test.com\",\"password\":\"password\"}";
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"));
    }
}