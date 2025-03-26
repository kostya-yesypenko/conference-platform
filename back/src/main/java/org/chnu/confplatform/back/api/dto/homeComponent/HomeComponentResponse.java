package org.chnu.confplatform.back.api.dto.homeComponent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.chnu.confplatform.back.api.dto.AbstractResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class HomeComponentResponse extends AbstractResponse {
    private String title;
    private String phone;
    private String email;
}
