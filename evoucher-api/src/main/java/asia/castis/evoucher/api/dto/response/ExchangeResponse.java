package asia.castis.evoucher.api.dto.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ExchangeResponse {
    private boolean isSuccess;
    private String approvementNo;
    private String paymentHistoryId;
}
