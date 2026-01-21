package asia.castis.evoucherservicefe.voucherhandler.dto;

import asia.castis.evoucherservicefe.common.enums.EnumVoucherStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsingVoucherRequest {
    private String exchangeType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transactionDate;
    private String storeId;
    @JsonProperty("voucherId")
    private String ev;
    private String voucherTypeCode;
    private Integer goodsId;
    private String goodsName;
    private Double listPrice;
    private Double discountRate;
    private Double discountAmount;
    private Double exchangeAmount;
    private String userMobileNumber;
    private String staffMobileNumber;
    private EnumVoucherStatus voucherStatusCode;
    private Double balance;

}
