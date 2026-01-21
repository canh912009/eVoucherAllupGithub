package asia.castis.web_hook.bean.dto.request;

import asia.castis.web_hook.common.VoucherStatusCode;
import asia.castis.web_hook.utils.Common;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsingVoucherRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Common.DATETIME_FORMAT_STR)
    private Date transactionDate;
    private String storeId;
    @JsonProperty("voucherId")
    private String ev;
    private Double exchangeAmount;
    private String userMobileNumber;
    private String staffMobileNumber;
    private VoucherStatusCode voucherStatusCode;

    public String toJsonString() {
        try {
            return Common.OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return this.toString();
        }
    }

}
