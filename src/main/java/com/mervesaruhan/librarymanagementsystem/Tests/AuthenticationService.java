package com.mervesaruhan.librarymanagementsystem.Tests;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.AuthResponse;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.AuthRequest;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.RegisterRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;
import com.mervesaruhan.librarymanagementsystem.model.enums.RoleEnum;
import com.mervesaruhan.librarymanagementsystem.repository.UserRepository;
import com.mervesaruhan.librarymanagementsystem.security.JwtUtil;
import com.mervesaruhan.librarymanagementsystem.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse authenticate(AuthRequest request) {
        // Spring Security authentication
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // Kullanıcıyı statik ya da DB'den getir
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        // Token üret
        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponse(token);
    }

    public AuthResponse register(RegisterRequestDto request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("A user with this username already exists. Please create a different username.");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw  new IllegalArgumentException("This email address is already in use by another user");
        }

        User user = User.builder()
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .phone(request.phone())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(RoleEnum.ROLE_PATRON)
                .active(true)
                .build();

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token);
    }
}