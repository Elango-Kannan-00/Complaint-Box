package com.cms.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    /**
     * Configure HTTP security for the application.
     * - CSRF is disabled for simplicity (suitable for API/dev only).
     * - All requests are permitted (no endpoint protection configured).
     */
    http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll())
        .httpBasic(Customizer.withDefaults());

    return http.build();
    }
}
