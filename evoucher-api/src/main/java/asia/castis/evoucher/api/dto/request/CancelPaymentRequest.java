package asia.castis.evoucher.api.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CancelPaymentRequest {
    private String paymentHistoryId;
    private String storeId;
    private int posType;
    private String posCd;
    private String approvementNo;
    private String posVerType;
}
