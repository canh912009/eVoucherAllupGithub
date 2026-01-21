package asia.castis.web_hook.bean.dto;

import asia.castis.web_hook.common.VoucherStatusCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ExtVoucherUsingInfo {
    VoucherStatusCode status;
    Date usingTime;
}
