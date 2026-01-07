package com.cloud.auth.controller;

import com.cloud.auth.domain.UserRequestDTO;
import com.cloud.auth.domain.UserResponseDTO;
import com.cloud.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO dto){
        UserResponseDTO responseDTO = userService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
