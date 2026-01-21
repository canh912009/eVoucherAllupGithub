package asia.castis.evoucherservicefe.common.dto.createmessageresult;

import asia.castis.evoucherservicefe.common.enums.EnumResendResult;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ResendResult {
    private Integer voucherResendHistoryId;
    private String frontEndPreviousPublishDetailStatusCode;
    private EnumResendResult frontEndUpdateResult;
}
