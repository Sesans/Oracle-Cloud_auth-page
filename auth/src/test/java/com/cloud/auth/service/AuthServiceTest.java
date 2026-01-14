package com.cloud.auth.service;

import com.cloud.auth.domain.Role;
import com.cloud.auth.domain.User;
import com.cloud.auth.domain.UserOrigin;
import com.cloud.auth.domain.dto.UserLoginDTO;
import com.cloud.auth.domain.dto.UserResponseDTO;
import com.cloud.auth.exception.InvalidCredentials;
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

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    TokenService tokenService;
    @InjectMocks
    AuthService authService;
    User user = new User();
    UserLoginDTO dto;

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
    }

    @Test
    void login_shouldLoginSuccessfully(){
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenService.generateToken(user)).thenReturn("valid-token");

        UserResponseDTO response = authService.login(dto);

        assertNotNull(response);
        assertEquals("valid-token", response.token());
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
    void login_shouldThrownInvalidCredentials_whenPrincipalIsInvalid(){
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn("Not a user object");
        when(authenticationManager.authenticate(any())).thenReturn(auth);

        InvalidCredentials ex = assertThrows(InvalidCredentials.class,
                () -> authService.login(dto));
        assertEquals("Authentication error", ex.getMessage());
    }
}