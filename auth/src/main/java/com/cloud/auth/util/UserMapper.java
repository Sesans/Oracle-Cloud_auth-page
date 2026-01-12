package com.cloud.auth.util;

import com.cloud.auth.domain.*;
import com.cloud.auth.domain.dto.UserRegisterDTO;
import com.cloud.auth.domain.dto.UserResponseDTO;

import java.time.LocalDateTime;

public class UserMapper {
    public static User requestToEntity(UserRegisterDTO dto){
        return new User(
                null,
                dto.firstName(),
                dto.lastName(),
                dto.email(),
                dto.password(),
                Role.USER,
                UserOrigin.LOCAL,
                LocalDateTime.now()
        );
    }

    public static UserResponseDTO entityToResponseDTO(User user, String token){
        return new UserResponseDTO(
                user.getUuid(),
                user.getEmail(),
                token
        );
    }
}
