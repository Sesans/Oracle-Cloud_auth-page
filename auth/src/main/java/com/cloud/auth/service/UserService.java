package com.cloud.auth.service;

import com.cloud.auth.domain.*;
import com.cloud.auth.domain.dto.UserRegisterDTO;
import com.cloud.auth.domain.dto.UserResponseDTO;
import com.cloud.auth.exception.BusinessException;
import com.cloud.auth.repository.UserRepository;
import com.cloud.auth.security.TokenService;
import com.cloud.auth.util.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO save(UserRegisterDTO dto){
        if(userRepository.existsByEmail(dto.email()))
            throw new BusinessException("Email already registered!");

        User user = UserMapper.requestToEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.password()));
        User savedUser = userRepository.save(user);
        String token = tokenService.generateToken(savedUser);

        return UserMapper.entityToResponseDTO(savedUser, token);
    }

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