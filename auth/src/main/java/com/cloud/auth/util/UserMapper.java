package com.cloud.auth.util;


import com.cloud.auth.domain.Role;
import com.cloud.auth.domain.User;
import com.cloud.auth.domain.UserRequestDTO;
import com.cloud.auth.domain.UserResponseDTO;

import java.time.LocalDateTime;

public class UserMapper {
    public static User requestToEntity(UserRequestDTO dto){
        return new User(
                null,
                dto.firstName(),
                dto.lastName(),
                dto.email(),
                dto.password(),
                true,
                Role.USER,
                LocalDateTime.now()
        );
    }

    public static UserResponseDTO entityToResponseDTO(User user){
        return new UserResponseDTO(
                user.getUuid(),
                user.getEmail()
        );
    }
}
