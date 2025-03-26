package org.chnu.confplatform.back.api.dto.homeComponent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.chnu.confplatform.back.api.dto.AbstractRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class HomeComponentRequest extends AbstractRequest {
    private String title;
    private String phone;
    private String email;
}
