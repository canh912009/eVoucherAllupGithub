package com.castis.pos_api.dto.request.third_party;

import com.castis.pos_api.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceBeUsingVoucherReq {
    @DateTimeFormat(pattern = Constants.FORMAT_DATE_TIME)
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    private Date transactionDate;
    private String storeId;
    private String voucherId;
    private Double exchangeAmount;
    private String staffMobileNumber;
}
