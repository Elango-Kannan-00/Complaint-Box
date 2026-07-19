package com.cms.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String userRegister(@Valid @RequestBody UserRegistrationDto dto) {
        return service.register(dto);
    }

    /**
     * Authenticate a user. Delegates to `AuthService.login`.
     */
    @PostMapping("/login")
    public String userLogin(@Valid @RequestBody UserLoginDto dto) {
        return service.login(dto);
    }
}
