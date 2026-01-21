package asia.castis.evoucher.api.dto.response;

import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherResponseWrapper {
    private Integer extPinId;
    private String extPinNo;
    private String extPinType;
    private String displayType;
    private String serialNo;

    private String system;
    private String contentLink;
    private String contentImagePath;
    private String contentImageName;
    private String externalPinPassword;
    private VoucherResponse voucher;

    // Those properties set manually
    private List<PaymentHistoryResponse> listPaymentHistoryResponse;
    private String otp;
    private long expireTime;

}
