package com.smartpark.service.security;

import com.smartpark.service.configuration.SecurityProperties;
import com.smartpark.service.dto.request.LoginRequest;
import com.smartpark.service.dto.response.LoginResponse;
import com.smartpark.service.exception.InvalidCredentialsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final SecurityProperties securityProperties;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        if (!securityProperties.getUsername().equals(request.getUsername())
                || !securityProperties.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(request.getUsername());

        return LoginResponse.builder().token(token).build();
    }
}
