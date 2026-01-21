package com.castis.publishservice.dto.request.ur_box;

import com.castis.publishservice.dto.UrBoxDataBuy;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Accessors(chain = true)
public class UrBoxSignData {
    String app_id;
    String app_secret;
    String campaign_code;
    List<UrBoxDataBuy> dataBuy;
    Integer isSendSms =0;
    String site_user_id;
    String transaction_id;
}
