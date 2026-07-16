package com.smartpark.service.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    // TODO: Add expiresAt
    private String token;
}
