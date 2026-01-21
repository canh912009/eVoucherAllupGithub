package com.evoucher.adminapi.settlement.service.model;

import com.evoucher.adminapi.common.enums.DirectionSort;
import com.evoucher.adminapi.serializer.EndDateWithoutTimeDeserializer;
import com.evoucher.adminapi.serializer.StartDateWithoutTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterSearchSettlement {
    private String companyName;
    private String settlementTarget;
    private String voucherTypeCode;
    private String settlementMethodCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = StartDateWithoutTimeDeserializer.class)
    private Date transactionStartDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = EndDateWithoutTimeDeserializer.class)
    private Date transactionEndDate;
    private String campaignId;
    private String campaignName;
    private String publishId;
    private String publishName;
    private Integer page;
    private Integer pageSize;
    private String sort;
    private DirectionSort direction;
}
