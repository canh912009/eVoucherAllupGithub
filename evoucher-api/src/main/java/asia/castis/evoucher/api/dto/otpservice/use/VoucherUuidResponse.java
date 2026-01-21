package asia.castis.evoucher.api.dto.otpservice.use;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherUuidResponse {
    private String error;
    private String path;
    private Data data;
}

