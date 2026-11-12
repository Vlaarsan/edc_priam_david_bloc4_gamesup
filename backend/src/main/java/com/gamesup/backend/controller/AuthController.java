package com.gamesup.backend.controller;

import com.gamesup.backend.dto.AuthRequestDTO;\nimport com.gamesup.backend.dto.UserDTO;
import com.gamesup.backend.dto.AuthResponseDTO;
import com.gamesup.backend.security.JwtService;
import com.gamesup.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(request.getUsername())
                .password("")
                .authorities("ROLE_CLIENT")
                .build();
        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(token)
                .username(request.getUsername())
                .role("ROLE_CLIENT")
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody com.gamesup.backend.dto.UserRequestDTO request) {
        com.gamesup.backend.dto.UserDTO user = userService.register(request);
        return ResponseEntity.ok(user);
    }
}

