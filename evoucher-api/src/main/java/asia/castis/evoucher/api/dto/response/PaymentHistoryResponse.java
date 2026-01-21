package asia.castis.evoucher.api.dto.response;

import asia.castis.evoucher.api.dto.request.PaymentHistory;
import asia.castis.evoucher.api.elastic.model.StoreModel;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PaymentHistoryResponse {
    private PaymentHistory paymentHistory;
    private StoreModel storeModel;
    private long timeExpireCancel;
}
