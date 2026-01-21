package com.castis.pos_api.dto.request;

import com.castis.pos_api.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseRequest {
    @DateTimeFormat(pattern = Constants.FORMAT_DATE_TIME)
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    Date requestTime;
    @NotBlank(message = "Brand ID is required")
    String brandId;
    @NotBlank(message = "Store ID is required")
    String storeId;
    String posCd;
}
