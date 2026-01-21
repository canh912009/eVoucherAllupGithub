package asia.castis.evoucher.api.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemType {
    INTERNAL,
    EXTERNAL,
    CHOICE,
    BULK,
    VNPT_EPAY,
    GIFTPOP,
    UR_BOX,
    WATANE
}
