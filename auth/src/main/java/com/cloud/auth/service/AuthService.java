package com.cloud.auth.service;

import com.cloud.auth.domain.RefreshToken;
import com.cloud.auth.domain.User;
import com.cloud.auth.domain.dto.AuthResponseWrapper;
import com.cloud.auth.domain.dto.UserLoginDTO;
import com.cloud.auth.domain.dto.UserResponseDTO;
import com.cloud.auth.exception.InvalidCredentials;
import com.cloud.auth.repository.RefreshTokenRepository;
import com.cloud.auth.security.RefreshTokenService;
import com.cloud.auth.security.TokenService;
import com.cloud.auth.util.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository tokenRepository;

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService, RefreshTokenService refreshTokenService, RefreshTokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.tokenRepository = tokenRepository;
    }

    public AuthResponseWrapper login(UserLoginDTO dto) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
            var auth = authenticationManager.authenticate(usernamePassword);

            User user = (User) auth.getPrincipal();

            String token = tokenService.generateToken(user);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(Duration.ofDays(7).toSeconds())
                    .sameSite("Strict")
                    .build();

            UserResponseDTO responseDTO = UserMapper.entityToResponseDTO(user, token);

            return new AuthResponseWrapper(responseDTO, cookie);
        } catch (AuthenticationException e) {
            throw new InvalidCredentials("Invalid email or password!");
        }
    }

}