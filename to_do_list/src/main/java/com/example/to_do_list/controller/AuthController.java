package com.example.to_do_list.controller;


import com.example.to_do_list.dto.AuthResponse;
import com.example.to_do_list.dto.LoginRequest;
import com.example.to_do_list.dto.RegisterRequest;
import com.example.to_do_list.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

        // Endpoint: POST /auth/register

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }


     // Endpoint: POST /auth/login


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // Endpoint: POST /auth/logout
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
         return ResponseEntity.ok().build();
    }
}
