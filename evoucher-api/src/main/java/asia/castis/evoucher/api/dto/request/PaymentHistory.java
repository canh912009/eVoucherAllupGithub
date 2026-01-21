package asia.castis.evoucher.api.dto.request;

import asia.castis.evoucher.api.common.enums.TransferStatusCode;
import asia.castis.evoucher.api.common.enums.VnptCardAction;
import asia.castis.evoucher.api.common.enums.VoucherStatusCode;
import asia.castis.evoucher.api.common.enums.VoucherTypeCode;
import asia.castis.evoucher.api.elastic.enums.EnumExchangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "#{@environment.getProperty('elastic.payment.history.indexName')}")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class PaymentHistory {
    private String id;
    private String transactionDate;
    private String storeId;
    private String voucherId;
    private long goodsId;
    private String goodsName;
    private Double listPrice;
    private Double discountRate;
    private Double discountAmount;
    private Double exchangeAmount;
    private String userMobileNumber;
    private String staffMobileNumber;
    private int posType;
    private String approvementNo;
    private Double balance;
    private Double initAmount;
    private String posCd;
    private Integer optInputType;
    private String posVerType;
    private EnumExchangeType exchangeType;
    private VoucherTypeCode voucherTypeCode;
    private VoucherStatusCode voucherStatus;
    private TransferStatusCode voucherTransferStatus;

    private VnptCardAction vnptExchangeType;
    private String vnptProviderCode;
    private String vnptReceiverPhoneNo;
}
