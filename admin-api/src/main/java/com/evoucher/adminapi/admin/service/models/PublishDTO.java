package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.cms.service.models.DownloadedVouchersDTO;
import com.evoucher.adminapi.cms.service.models.EndUserDTO;
import com.evoucher.adminapi.cms.service.models.GoodsDTO;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PublishDTO extends BaseDTO {
    private Integer id;
    private CampaignDTO campaign;
    private GoodsDTO goods;
    private String publishName;
    private String messageSubject;
    private String messageContent;
    private String messageCallingNumber;
    private String bookingYn;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date bookingDate;
    private String testSendYn;
    private String receiverNoDuplicateAllowYn;
    private String uploadType;
    private String uploadText;
    private String uploadFilePath;
    private String uploadFileName;
    private List<EndUserDTO> endUsers;
    private String smsType;
    private String supplierId;
    private String customerId;
    private Double sellPrice;
    private Double sellListPrice;
    private Double sellDiscountRate;
    private Double sellDiscountAmount;
    private Double sellCommissionRate;
    private String sellVatIncludeYn;
    private String sellSettlementMethodCode;
    private Double sendCost;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date cancelDate;
    private String statusCode;
    private String approveStatusCode;
    private String approveRequestId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date approveRequestDate;
    private String approveId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date approveDate;
    private String rejectId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date rejectDate;
    private String rejectReason;
    private String contentLink;
    private String contentImagePath;
    private String contentImageName;
    private String senderName;
    private Integer numberOfVouchers;
    private EnumValidYn showPopupYn;
    private List<DownloadedVouchersDTO> downloadVouchers;
    private CustomerDTO customer;
}
