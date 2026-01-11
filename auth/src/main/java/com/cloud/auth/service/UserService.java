package com.cloud.auth.service;

import com.cloud.auth.domain.*;
import com.cloud.auth.repository.UserRepository;
import com.cloud.auth.security.TokenService;
import com.cloud.auth.util.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public UserResponseDTO save(UserRegisterDTO dto){
        User user = UserMapper.requestToEntity(dto);
        userRepository.save(user);
        String token = tokenService.generateToken(user);

        return UserMapper.entityToResponseDTO(user, token);
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