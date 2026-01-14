package com.cloud.auth.controller;

import com.cloud.auth.domain.dto.AuthResponseWrapper;
import com.cloud.auth.domain.dto.UserLoginDTO;
import com.cloud.auth.domain.dto.UserRegisterDTO;
import com.cloud.auth.domain.dto.UserResponseDTO;
import com.cloud.auth.exception.BusinessException;
import com.cloud.auth.security.RefreshTokenService;
import com.cloud.auth.service.AuthService;
import com.cloud.auth.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserService userService, AuthService authService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegisterDTO dto){
        var authData = userService.save(dto);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authData.cookie().toString())
                .body(authData.responseDTO());
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid UserLoginDTO dto){
        var authData = authService.login(dto);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authData.cookie().toString())
                .body(authData.responseDTO());
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserResponseDTO> refresh(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new BusinessException("Refresh Token missing"));

        AuthResponseWrapper updatedAuth = refreshTokenService.refreshSession(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, updatedAuth.cookie().toString())
                .body(updatedAuth.responseDTO());
    }
}
