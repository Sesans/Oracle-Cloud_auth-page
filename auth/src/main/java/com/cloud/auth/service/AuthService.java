package com.cloud.auth.service;

import com.cloud.auth.domain.User;
import com.cloud.auth.domain.dto.UserLoginDTO;
import com.cloud.auth.domain.dto.UserResponseDTO;
import com.cloud.auth.exception.InvalidCredentials;
import com.cloud.auth.security.TokenService;
import com.cloud.auth.util.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public UserResponseDTO login(UserLoginDTO dto) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
            var auth = authenticationManager.authenticate(usernamePassword);

            if(!(auth.getPrincipal() instanceof User user))
                throw new InvalidCredentials("Authentication error");

            String token = tokenService.generateToken(user);
            return UserMapper.entityToResponseDTO(user, token);
        } catch (AuthenticationException e) {
            throw new InvalidCredentials("Invalid email or password!");
        }
    }

}