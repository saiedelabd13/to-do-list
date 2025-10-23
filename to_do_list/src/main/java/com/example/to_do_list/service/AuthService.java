package com.example.to_do_list.service;

import com.example.to_do_list.dto.AuthResponse;
import com.example.to_do_list.dto.LoginRequest;
import com.example.to_do_list.dto.RegisterRequest;
import com.example.to_do_list.entity.User;
import com.example.to_do_list.exception.UnauthorizedException;
import com.example.to_do_list.repository.UserRepository;
import com.example.to_do_list.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }


    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UnauthorizedException("Invalid credentials."));

            String jwtToken = jwtService.generateToken(user);

            return AuthResponse.builder()
                    .accessToken(jwtToken)
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();

        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Invalid email or password.");
        }
    }
}
