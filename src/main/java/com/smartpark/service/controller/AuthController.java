package com.smartpark.service.controller;

import com.smartpark.service.dto.request.LoginRequest;
import com.smartpark.service.dto.response.LoginResponse;
import com.smartpark.service.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        System.out.println(">>> Login Controller reached");
        return authenticationService.login(request);
    }

}
