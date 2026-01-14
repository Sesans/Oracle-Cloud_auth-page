package com.cloud.auth.service;

import com.cloud.auth.domain.*;
import com.cloud.auth.domain.dto.UserRegisterDTO;
import com.cloud.auth.domain.dto.UserResponseDTO;
import com.cloud.auth.exception.BusinessException;
import com.cloud.auth.repository.UserRepository;
import com.cloud.auth.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenService tokenService;
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
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO responseDTO = userService.save(requestDTO);

        assertEquals(user.getEmail(), responseDTO.email());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void save_shouldThrowBusinessException(){
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> userService.save(requestDTO));

        assertEquals("Email already registered!", ex.getMessage());
    }

    @Test
    void upsert_shouldInsertTheUserSuccessfully(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User user1 = userService.upsert(user.getFirstName(), user.getEmail(), user.getUserOrigin().toString());

        assertEquals(user1.getFirstName(), user.getFirstName());
        assertEquals(user1.getEmail(), user.getEmail());
        assertEquals(user1.getUserOrigin(), user.getUserOrigin());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void upsert_shouldUpdateTheUserSuccessfully(){
        String newName = "Novo Nome";
        String email = user.getEmail();
        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user1 = userService.upsert(newName, email, "GOOGLE");

        assertNotNull(user1);
        assertEquals(newName, user1.getFirstName());
        assertEquals(email, user1.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }
}