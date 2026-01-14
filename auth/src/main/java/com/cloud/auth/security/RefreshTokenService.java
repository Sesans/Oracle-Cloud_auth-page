package com.cloud.auth.security;

import com.cloud.auth.domain.RefreshToken;
import com.cloud.auth.domain.User;
import com.cloud.auth.domain.dto.AuthResponseWrapper;
import com.cloud.auth.domain.dto.UserResponseDTO;
import com.cloud.auth.repository.RefreshTokenRepository;
import com.cloud.auth.util.UserMapper;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository tokenRepository;
    private final TokenService tokenService;

    public RefreshTokenService(RefreshTokenRepository tokenRepository, TokenService tokenService) {
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
    }

    public RefreshToken createRefreshToken(User user){
        tokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plus(Duration.ofHours(3)));
        refreshToken.setToken(UUID.randomUUID().toString());

        return tokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().isBefore(Instant.now())){
            tokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please login again");
        }

        return token;
    }

    public AuthResponseWrapper refreshSession(String refreshToken) {
        return tokenRepository.findByToken(refreshToken)
                .map(this::verifyExpiration)
                .map(token -> {
                    User user = ((RefreshToken) token).getUser();

                    String newAccessToken = tokenService.generateToken(user);

                    RefreshToken newRefreshToken = createRefreshToken(user);
                    ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken.getToken())
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(Duration.ofHours(3).toSeconds())
                            .sameSite("Strict")
                            .build();

                    UserResponseDTO responseDTO = UserMapper.entityToResponseDTO(user, newAccessToken);
                    return new AuthResponseWrapper(responseDTO, cookie);
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found in database"));
    }
}
