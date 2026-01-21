package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.serializer.EndDateWithoutTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CsManagementFilterRequest {
    String startDate;
    String endDate;
    Date startDateObject;
    Date endDateObject;
    String pinStatus;
    String deliveryName;
    String campaignName;
    Set<String> pins;
    String deliveryId;
    Long campaignId;
    Set<String> targetNumbers;
    Set<String> targetNames;
    String voucherUUID;
    String deliveryDate;
    Integer page;
    Integer pageSize;
    @NotNull
    String sort;
    @NotNull
    Sort.Direction direction;
    String serialNo;
    String productName;
    Long productId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = EndDateWithoutTimeDeserializer.class)
    Date voucherExpireBefore;
}
