package asia.castis.evoucher.api.elastic.model.publish;

import asia.castis.evoucher.api.common.enums.VoucherStatusCode;
import asia.castis.evoucher.api.common.enums.VoucherTypeCode;
import asia.castis.evoucher.api.elastic.enums.EnumExchangeType;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class RabbitPaymentHistory {
    public RabbitPaymentHistory(String id, EnumExchangeType exchangeType, String transactionDate, String storeId,
                                String voucherId, VoucherTypeCode voucherTypeCode, long goodsId, String goodsName,
                                double listPrice, double discountRate, double discountAmount, double exchangeAmount,
                                String userMobileNumber, String staffMobileNumber, VoucherStatusCode voucherStatus, double balance) {
        this.id = id;
        this.exchangeType = exchangeType;
        this.transactionDate = transactionDate;
        this.storeId = storeId;
        this.voucherId = voucherId;
        this.voucherTypeCode = voucherTypeCode;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.listPrice = listPrice;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.exchangeAmount = exchangeAmount;
        this.userMobileNumber = userMobileNumber;
        this.staffMobileNumber = staffMobileNumber;
        this.voucherStatusCode = voucherStatus;
        this.balance = balance;
    }

    private String id;
    private EnumExchangeType exchangeType;
    private String transactionDate;
    private String storeId;
    private String voucherId;
    private VoucherTypeCode voucherTypeCode;
    private long goodsId;
    private String goodsName;
    private double listPrice;
    private double discountRate;
    private double discountAmount;
    private double exchangeAmount;
    private String userMobileNumber;
    private String staffMobileNumber;
    private VoucherStatusCode voucherStatusCode;
    private double balance;
}
