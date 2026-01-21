package asia.castis.web_hook.bean.dto.request;

import asia.castis.web_hook.common.UpdatingVoucherType;
import asia.castis.web_hook.utils.Common;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatingVoucherReq {
    @DateTimeFormat(pattern = Common.DATETIME_FORMAT_STR)
    @JsonFormat(pattern = Common.DATETIME_FORMAT_STR)
    Date transactionDate;
    String storeId;
    String voucherId;
    @Setter
    Double exchangeAmount;
    String staffMobileNumber;
    UpdatingVoucherType updatingType;
}
