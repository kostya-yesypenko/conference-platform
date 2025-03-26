package org.chnu.confplatform.back.api.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.chnu.confplatform.back.api.dto.AbstractRequest;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserRequest extends AbstractRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    private String password;

    private String role;

    private boolean archived;
}
