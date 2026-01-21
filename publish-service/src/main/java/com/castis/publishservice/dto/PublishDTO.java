package com.castis.publishservice.dto;

import com.castis.publishservice.entity.Campaign;
import com.castis.publishservice.entity.Customer;
import com.castis.publishservice.utils.status.PublishStatus;
import lombok.Data;

import java.util.Date;

@Data
public class PublishDTO {
    private Long id;
    private Campaign campaign;
    private GoodsDTO good;
    private String publishName;
    private String messageSubject;
    private String messageContent;
    private String messageCallingNumber;
    private String bookingYn;
    private Date bookingDate;
    private String testSendYn;
    private String receiverNoDuplicateAllowYn;
    private String uploadType;
    private String uploadFileName;
    private String uploadText;
    private String smsType;
    private String supplierId;
    private Customer customer;
    private Double sellPrice;
    private Double sellListPrice;
    private Double sellDiscountRate;
    private Double sellDiscountAmount;
    private Double sellCommissionRate;
    private String sellVatIncludeYn;
    private String sellSettlementMethodCode;
    private Double sendCost;
    private Date publishDate;
    private Date cancelDate;
    private PublishStatus publishStatusCode;
    private String approveStatusCode;
    private String approveRequestId;
    private Date approveRequestDate;
    private String approveId;
    private Date approveDate;
    private String rejectId;
    private Date rejectDate;
    private String rejectReason;
}
