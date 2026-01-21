package asia.castis.evoucher.api.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SMSType {
    SMS,
    ZALO,
    DOWNLOAD,
    PAPER,
    EMAIL;
}
