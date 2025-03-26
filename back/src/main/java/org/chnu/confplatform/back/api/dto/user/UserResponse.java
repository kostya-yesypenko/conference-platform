package org.chnu.confplatform.back.api.dto.user;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.chnu.confplatform.back.api.dto.AbstractResponse;

import javax.validation.constraints.NotBlank;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserResponse extends AbstractResponse {

    private String firstName;
    private String middleName;
    private String lastName;
    @NotBlank
    private String email;
    private String phone;
    private String role;
    private String scienceRank;
    private String country;
    private String city;
    private String work;
    private boolean archived;
}
