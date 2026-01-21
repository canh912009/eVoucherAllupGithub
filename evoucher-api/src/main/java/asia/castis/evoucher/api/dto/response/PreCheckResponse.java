package asia.castis.evoucher.api.dto.response;

import asia.castis.evoucher.api.common.enums.ApiVersion;
import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreCheckResponse {
    private ApiVersion version;
    private Boolean activated;
    private String phoneNumber;
    private String voucherImageUrl;
    private String voucherName;
    private String ev;

    private Boolean otpRequired;
    private Boolean otpExists;
    private String otpExpireDt;
}
