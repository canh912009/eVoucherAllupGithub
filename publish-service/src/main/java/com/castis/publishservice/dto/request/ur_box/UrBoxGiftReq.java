package com.castis.publishservice.dto.request.ur_box;

import com.castis.publishservice.dto.UrBoxDataBuy;
import com.castis.publishservice.utils.Constants;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Accessors(chain = true)
public class UrBoxGiftReq{
    String app_secret;
    String app_id;
    String campaign_code;
    String site_user_id;
    String ttphone;
    String ttemail;
    String ttfullname;
    String transaction_id;
    Integer shorten;
    Integer isSendSms = 0;
    String pin;
    List<UrBoxDataBuy> dataBuy;
    public String toString() {
        return Constants.gson.toJson(this);
    }
}


