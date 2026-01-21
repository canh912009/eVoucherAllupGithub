package asia.castis.evoucher.api.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumResult {
    SUCCESS,
    FAILED,
    SUCCESS_AFTER_RETRY,
    FAILED_AFTER_RETRY,
    PROCESSING,
}
