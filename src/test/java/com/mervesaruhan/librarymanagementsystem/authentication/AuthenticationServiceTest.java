package com.mervesaruhan.librarymanagementsystem.authentication;

import com.mervesaruhan.librarymanagementsystem.general.UserTestDataGenerator;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.AuthResponse;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.AuthRequest;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.RegisterRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;
import com.mervesaruhan.librarymanagementsystem.repository.UserRepository;
import com.mervesaruhan.librarymanagementsystem.security.CustomUserDetailsService;
import com.mervesaruhan.librarymanagementsystem.security.JwtUtil;
import com.mervesaruhan.librarymanagementsystem.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void shouldRegisterWithPatronRole() {

        RegisterRequestDto request = UserTestDataGenerator.createRegisterRequestDto();
        User user = UserTestDataGenerator.createUserPatron();

        Mockito.when(userRepository.existsByUsername(request.username())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(request.email())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn(user.getPassword());
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDetails mockDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any()))
                .thenReturn(mockDetails);

        Mockito.when(jwtUtil.generateToken(Mockito.any())).thenReturn("mockedToken");

        AuthResponse response = authenticationService.register(request);

        Assertions.assertEquals("mockedToken", response.token());
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }


    @Test
    void shouldNotRegister_WhenUsernameExists() {
        RegisterRequestDto request = UserTestDataGenerator.createRegisterRequestDto();

        Mockito.when(userRepository.existsByUsername(Mockito.any())).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> authenticationService.register(request));
    }

    @Test
    void shouldAuthenticate() {
        AuthRequest request = UserTestDataGenerator.createAuthRequest();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.username(), request.password());

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(jwtUtil.generateToken(Mockito.any())).thenReturn("mocked-jwt-token");

        AuthResponse response = authenticationService.authenticate(request);
        Assertions.assertEquals("mocked-jwt-token", response.token());
    }
}