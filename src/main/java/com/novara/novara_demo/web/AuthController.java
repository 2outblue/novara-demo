package com.novara.novara_demo.web;

import com.novara.novara_demo.model.dto.UserLoginDTO;
import com.novara.novara_demo.service.AuthService;
import com.novara.novara_demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final AuthService authService;

    public AuthController(AuthenticationManager authManager, AuthService authService) {
        this.authManager = authManager;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<OAuth2AccessTokenResponse> login(@RequestBody UserLoginDTO loginDTO) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        String token = authService.getJwt(authentication);
        return ResponseEntity.ok(OAuth2AccessTokenResponse.withToken(token).tokenType(OAuth2AccessToken.TokenType.BEARER).build());
    }

}

