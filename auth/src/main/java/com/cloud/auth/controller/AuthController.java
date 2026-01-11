package com.cloud.auth.controller;

import com.cloud.auth.domain.UserLoginDTO;
import com.cloud.auth.domain.UserRegisterDTO;
import com.cloud.auth.domain.UserResponseDTO;
import com.cloud.auth.service.AuthService;
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
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRegisterDTO dto){
        UserResponseDTO responseDTO = userService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO dto){
        UserResponseDTO responseDTO = authService.login(dto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
