package com.mervesaruhan.librarymanagementsystem.controller;

import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.AuthRequest;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.AuthResponse;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.RegisterRequestDto;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import com.mervesaruhan.librarymanagementsystem.Tests.AuthenticationService;
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

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RestResponse<AuthResponse>> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(RestResponse.of(authenticationService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<RestResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(RestResponse.of(authenticationService.authenticate(request)));
    }
}
