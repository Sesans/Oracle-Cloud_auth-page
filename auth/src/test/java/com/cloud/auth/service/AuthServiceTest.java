package com.cloud.auth.service;

import com.cloud.auth.domain.RefreshToken;
import com.cloud.auth.domain.Role;
import com.cloud.auth.domain.User;
import com.cloud.auth.domain.UserOrigin;
import com.cloud.auth.domain.dto.AuthResponseWrapper;
import com.cloud.auth.domain.dto.UserLoginDTO;
import com.cloud.auth.exception.InvalidCredentials;
import com.cloud.auth.repository.RefreshTokenRepository;
import com.cloud.auth.security.RefreshTokenService;
import com.cloud.auth.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthServiceTest {
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    TokenService tokenService;
    @Mock
    RefreshTokenRepository tokenRepository;
    @Mock
    RefreshTokenService refreshTokenService;
    @InjectMocks
    AuthService authService;

    User user = new User();
    UserLoginDTO dto;
    RefreshToken refreshToken;

    @BeforeEach
    void setUp(){
        user.setUuid(UUID.randomUUID());
        user.setFirstName("leo");
        user.setLastName("santos");
        user.setEmail("leo@gmail.com");
        user.setPassword("1234");
        user.setUserOrigin(UserOrigin.LOCAL);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        dto = new UserLoginDTO("test", "test");
        refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
    }

    @Test
    void login_shouldLoginSuccessfully(){
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenService.generateToken(user)).thenReturn("valid-token");
        when(refreshTokenService.createRefreshToken(any(User.class))).thenReturn(refreshToken);

        AuthResponseWrapper response = authService.login(dto);

        assertNotNull(response);
        assertEquals("valid-token", response.responseDTO().token());
        verify(tokenService).generateToken(user);
    }

    @Test
    void login_shouldThrowInvalidCredentials_whenAuthenticationFails(){
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        InvalidCredentials ex = assertThrows(InvalidCredentials.class,
                () -> authService.login(dto));
        assertEquals("Invalid email or password!", ex.getMessage());
    }

    @Test
    void login_shouldThrownInvalidCredentials_whenAuthenticationFails(){
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        InvalidCredentials ex = assertThrows(InvalidCredentials.class,
                () -> authService.login(dto));

        assertEquals("Invalid email or password!", ex.getMessage());
    }
}