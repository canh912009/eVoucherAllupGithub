package asia.castis.evoucher.api.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoucherStatusCode {
    NORMAL,
    EXPIRE,
    PART_USED,
    USED,
    DISABLED
}
