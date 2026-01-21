package asia.castis.evoucher.api.dto.request.vnpt;

import asia.castis.evoucher.api.common.enums.VnptCardAction;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VnptPurchaseRequest {
    @NotNull(message = "Vnpt voucher can not be null")
    @NotEmpty(message = "Vnpt voucher can not be empty")
    private String ev;
    @NotNull(message = "Provider can not be null")
    @NotEmpty(message = "Provider can not be empty")
    private String providerCode;
    @NotNull(message = "Action can not be null")
    @NotEmpty(message = "Action can not be empty")
    private String action;
    private String receiverPhoneNo;
}
