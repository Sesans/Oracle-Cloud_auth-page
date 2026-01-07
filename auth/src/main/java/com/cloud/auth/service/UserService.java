package com.cloud.auth.service;

import com.cloud.auth.domain.User;
import com.cloud.auth.domain.UserRequestDTO;
import com.cloud.auth.domain.UserResponseDTO;
import com.cloud.auth.repository.UserRepository;
import com.cloud.auth.util.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO save(UserRequestDTO dto){
        User user = UserMapper.requestToEntity(dto);
        userRepository.save(user);
        return UserMapper.entityToResponseDTO(user);
    }
}