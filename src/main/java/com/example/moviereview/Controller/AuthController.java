package com.example.moviereview.Controller;

import com.example.moviereview.Entity.Role;
import com.example.moviereview.Entity.User;
import com.example.moviereview.Repository.UserRepository;
import com.example.moviereview.exception.EmailAlreadyExistsException;
import com.example.moviereview.exception.UsernameAlreadyExistsException;
import com.example.moviereview.security.CustomUserDetailsService;
import com.example.moviereview.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(
             @Valid @RequestBody User request) {

        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new UsernameAlreadyExistsException(
                    "Username already exists");
        }

        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new EmailAlreadyExistsException(
                    "Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(
                "User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        UserDetails userDetails =
                userDetailsService
                        .loadUserByUsername(username);

        String token =
                jwtService.generateToken(userDetails);

        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        Map<String, String> response = new HashMap<>();

        response.put("token", token);
        response.put("role", role);
        response.put("username", username);

        return ResponseEntity.ok(response);
    }
}