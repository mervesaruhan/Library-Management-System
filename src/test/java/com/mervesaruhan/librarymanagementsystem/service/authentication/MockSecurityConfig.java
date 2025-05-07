package com.mervesaruhan.librarymanagementsystem.service.authentication;

import com.mervesaruhan.librarymanagementsystem.security.CustomUserDetailsService;
import com.mervesaruhan.librarymanagementsystem.security.JwtAuthenticationFilter;
import com.mervesaruhan.librarymanagementsystem.security.JwtUtil;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class MockSecurityConfig {

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return Mockito.mock(CustomUserDetailsService.class);
    }

    @Bean
    public JwtUtil jwtUtil() {
        return Mockito.mock(JwtUtil.class);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return Mockito.mock(JwtAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Mockito.mock(PasswordEncoder.class);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}