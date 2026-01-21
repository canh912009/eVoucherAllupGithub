package asia.castis.evoucher.api.dto.request.activate;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Data
public class ActivateRequestV1 extends ActivateRequest {
    @NotNull
    private String activationKey;
}
