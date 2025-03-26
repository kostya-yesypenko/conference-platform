package org.chnu.confplatform.back.api.dto.authorization;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class AuthorizationRequest {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
