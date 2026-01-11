package com.cloud.auth.security;

import com.cloud.auth.domain.User;
import com.cloud.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOauth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserService userService;

    public CustomOauth2SuccessHandler(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User auth2User = (OAuth2User) authentication.getPrincipal();
        String name = auth2User.getAttribute("name");
        String email = auth2User.getAttribute("email");

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String provider = oauthToken.getAuthorizedClientRegistrationId();

        User user = userService.upsert(name, email, provider);

        String token = tokenService.generateToken(user);

        response.sendRedirect("http://localhost:5173/auth/callback?token=" + token);
    }
}
