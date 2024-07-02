package com.movie.controller;

import com.movie.auth.entities.RefreshToken;
import com.movie.auth.entities.User;
import com.movie.auth.services.AuthService;
import com.movie.auth.services.JwtService;
import com.movie.auth.services.RefreshTokenService;
import com.movie.auth.utils.AuthResponse;
import com.movie.auth.utils.LoginRequest;
import com.movie.auth.utils.RefreshTokenRequest;
import com.movie.auth.utils.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>((authService.register(registerRequest)), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>((authService.login(loginRequest)), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);

        return new ResponseEntity<>((AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build()), HttpStatus.OK);
    }


}
