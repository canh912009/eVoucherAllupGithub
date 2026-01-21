package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.enums.UploadDataType;
import com.evoucher.adminapi.cms.service.models.request.EndUserRequest;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SettlementMethodCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublishRequest {
    @NotNull(message = "Campaign ID is empty!")
    private Integer campaignId;
    @NotBlank(message = "Publish name is empty!")
    private String publishName;
    @NotNull(message = "Sms type is empty!")
    private SMSType smsType;
    @NotNull(message = "Booking Y/N is empty!")
    private EnumValidYn bookingYn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bookingDate;
    @NotBlank(message = "Message subject is empty!")
    private String messageSubject;
    @NotBlank(message = "Message content is empty!")
    private String messageContent;
    private EnumValidYn receiverNoDuplicateAllowYn;
    private UploadDataType uploadType;
    private String uploadFileName;
    private String uploadFilePath;
    private List<EndUserRequest> endUsers;
    @NotNull(message = "Product is empty!")
    private Integer goodsId;
    @NotNull(message = "Sales price is empty!")
    @PositiveOrZero(message = "Sales price must be zero or positive")
    private Double sellPrice;
    @NotNull(message = "List price is empty!")
    @PositiveOrZero(message = "List price must be zero or positive")
    private Double sellListPrice;
    @NotNull(message = "Settlement method is empty!")
    private SettlementMethodCode sellSettlementMethodCode;
    @NotNull(message = "Discount amount is empty!")
    @PositiveOrZero(message = "Discount amount must be zero or positive")
    private Double sellDiscountAmount;
    @NotNull(message = "Commission rate is empty!")
    @PositiveOrZero(message = "Commission rate must be zero or positive")
    private Double sellCommissionRate;
    @NotNull(message = "Including VAT is empty!")
    private EnumValidYn sellVatIncludeYn;
    private ApproveStatus approveStatusCode;
    private String contentLink;
    private String contentImagePath;
    private String contentImageName;
    @NotNull(message = "Sender name is empty!")
    @Pattern(regexp = "^[a-zA-Z0-9\\s-,_().]*$", message = "Sender name only allow alphabet and number!")
    private String senderName;
    private Integer numberOfVouchers;
    private EnumValidYn showPopupYn;

}
