package com.cloud.auth.service;

import com.cloud.auth.domain.*;
import com.cloud.auth.domain.dto.UserRegisterDTO;
import com.cloud.auth.domain.dto.UserResponseDTO;
import com.cloud.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;

    UserRegisterDTO requestDTO;
    User user;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setUuid(UUID.randomUUID());
        user.setFirstName("leo");
        user.setLastName("santos");
        user.setEmail("leo@gmail.com");
        user.setPassword("1234");
        user.setUserOrigin(UserOrigin.LOCAL);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        requestDTO = new UserRegisterDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
    }

    @Test
    void save_shouldCreateUserSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO responseDTO = userService.save(requestDTO);

        assertEquals(user.getEmail(), responseDTO.email());

        verify(userRepository, times(1)).save(any(User.class));
    }
}