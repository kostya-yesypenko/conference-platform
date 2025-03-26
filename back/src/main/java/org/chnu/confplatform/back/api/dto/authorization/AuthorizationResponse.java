package org.chnu.confplatform.back.api.dto.authorization;


import lombok.Data;
import org.chnu.confplatform.back.api.dto.user.UserResponse;

@Data
public class AuthorizationResponse {

    private String token;

    private String refreshToken;

    private int accessTokenLifetimeMinutes;

    private int refreshTokenLifetimeMinutes;

    private String role;

    private UserResponse user;
}
