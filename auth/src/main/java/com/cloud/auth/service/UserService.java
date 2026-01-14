package com.cloud.auth.service;

import com.cloud.auth.domain.*;
import com.cloud.auth.domain.dto.AuthResponseWrapper;
import com.cloud.auth.domain.dto.UserRegisterDTO;
import com.cloud.auth.domain.dto.UserResponseDTO;
import com.cloud.auth.exception.BusinessException;
import com.cloud.auth.repository.UserRepository;
import com.cloud.auth.security.RefreshTokenService;
import com.cloud.auth.security.TokenService;
import com.cloud.auth.util.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public UserService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthResponseWrapper save(UserRegisterDTO dto){
        if(userRepository.existsByEmail(dto.email()))
            throw new BusinessException("Email already registered!");

        User user = UserMapper.requestToEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.password()));
        User savedUser = userRepository.save(user);
        String token = tokenService.generateToken(savedUser);
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
    }

    @Transactional
    public User upsert(String name, String email, String provider) {
        return userRepository.findByEmail(email)
                .map(existingUser -> {
                    existingUser.setFirstName(name);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setFirstName(name);
                    newUser.setEmail(email);
                    newUser.setRole(Role.USER);
                    newUser.setUserOrigin(UserOrigin.valueOf(provider.toUpperCase()));
                    return userRepository.save(newUser);
                });
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}