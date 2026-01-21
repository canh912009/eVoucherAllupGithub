package asia.castis.evoucher.api.elastic.response;

import asia.castis.evoucher.api.dto.response.PaymentHistoryResponse;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UserVoucherDetailResponse {
    private ElasticVoucherResponse voucher;
    private List<PaymentHistoryResponse> listPaymentHistoryResponse;
    private String otp;
    private long expireTime;

    private long extPinId;
    private String extPinNo;
    /*
     *  URBOX/VNPT/EXT_PIN/
     * */
    private String extPinType;
    /*
     * QR/BARCODE/TEXT/
     * */
    private String system;
    private String contentLink;
    private String contentImagePath;
    private String contentImageName;
    private String externalPinPassword;
}
