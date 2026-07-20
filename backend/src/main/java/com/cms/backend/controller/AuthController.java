package com.cms.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.cms.backend.dto.auth.UserResponseDto;
import com.cms.backend.dto.auth.UserLoginDto;
import com.cms.backend.dto.auth.UserRegistrationDto;
import com.cms.backend.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class AuthController {
    @Autowired
    private AuthService service;

    /**
     * Register a new user. Delegates to `AuthService.register`.
     */
    @PostMapping("/register")
    public UserResponseDto userRegister(@Valid @RequestBody UserRegistrationDto dto) {
        return service.register(dto);
    }

    /**
     * Authenticate a user. Delegates to `AuthService.login`.
     */
    @PostMapping("/login")
    public UserResponseDto userLogin(@Valid @RequestBody UserLoginDto dto) {
        return service.login(dto);
    }

    /**
     * Fetch a public user profile by email so the frontend can recover from partial sessions.
     */
    @GetMapping("/profile")
    public UserResponseDto userProfile(@RequestParam String email) {
        return service.getProfileByEmail(email);
    }
}
