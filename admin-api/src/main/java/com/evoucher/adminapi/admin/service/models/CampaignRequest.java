package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.cms.service.models.GoodsDTO;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseDTO;
import com.evoucher.adminapi.serializer.EndDateWithoutTimeDeserializer;
import com.evoucher.adminapi.serializer.StartDateWithoutTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRequest extends BaseDTO {
    @NotBlank(message = "Campaign name is empty!")
    private String campaignName;
    @NotBlank(message = "Customer ID is empty!")
    private String customerId;
    @NotNull(message = "Customer Contract ID is empty!")
    private Integer customerContractId;

    @NotNull(message = "Campaign start date is not empty!")
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = StartDateWithoutTimeDeserializer.class)
    private Date startDate;

    @NotNull(message = "Campaign end date is not empty!")
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = EndDateWithoutTimeDeserializer.class)
    private Date endDate;
    private String messageSubject;
    private String messageContent;
    private String messageCallingNumber;
    private List<GoodsDTO> goods;
    private ApproveStatus approveStatusCode;
    @NotNull(message = "Message Template Id can not be empty.")
    private Integer messageTemplateId;
    private String contentLink;
    private String contentImagePath;
    private String contentImageName;
    private String senderName;
    private EnumValidYn showPopupYn;
}
