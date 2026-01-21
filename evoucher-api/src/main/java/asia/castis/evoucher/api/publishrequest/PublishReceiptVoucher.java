package asia.castis.evoucher.api.publishrequest;

import asia.castis.evoucher.api.common.enums.VoucherTypeCode;
import asia.castis.evoucher.api.elastic.enums.EnumVoucherTransferStatus;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PublishReceiptVoucher {
    private EnumVoucherTransferStatus transferStatusCode;
    private String transactionDate;
    private String receiptConfirmDate;
    private String returnDate;
    private String fromVoucherShortLink;
    private String fromEv;
    private String fromMobileNumber;
    private String toVoucherShortLink;
    private String toEv;
    private String toMobileNumber;
    private VoucherTypeCode voucherTypeCode;
    private Double initAmount;
    private Double transferAmount;
    private EndUserBERequest endUser;
}
