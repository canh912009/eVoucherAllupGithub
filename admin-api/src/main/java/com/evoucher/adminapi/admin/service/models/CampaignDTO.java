package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.admin.enums.CampaignStatusCode;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.cms.service.models.GoodsDTO;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDTO extends BaseDTO {

    private Integer id;
    private String campaignName;
    private CustomerDTO customer;
    private CustomerContractDTO customerContract;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private String messageSubject;
    private String messageContent;
    private String messageCallingNumber;
    private String validYn;
    private List<GoodsDTO> listGoods;
    private List<PublishDTO> publishes;
    private String approveStatusCode;
    private String approveRequestId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date approveRequestDate;
    private String approveId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date approveDate;
    private String statusCode;
    MessageTemplateDTO messageTemplate;
    private String contentLink;
    private String contentImagePath;
    private String contentImageName;
    private String senderName;
    private EnumValidYn showPopupYn;

    public void setStatusCode(CampaignDTO campaignDTO) {
        if (Objects.isNull(campaignDTO.getApproveStatusCode())
                || Objects.isNull(campaignDTO.getStartDate())
                || Objects.isNull(campaignDTO.getEndDate())) return;

        ApproveStatus approveStatusCode = ApproveStatus.valueOf(campaignDTO.getApproveStatusCode());
        switch (approveStatusCode) {
            case REQ: {
                this.statusCode = CampaignStatusCode.WAIT_APPRV.toString();
                break;
            }
            case REJCT: {
                this.statusCode = CampaignStatusCode.REJECTED.toString();
                break;
            }
            case APPRV: {
                Date currentDate = new Date();
                if (currentDate.after(startDate) && currentDate.before(endDate)) {
                    this.statusCode = CampaignStatusCode.PROCESSING.toString();
                } else if (currentDate.after(endDate)){
                    this.statusCode = CampaignStatusCode.END.toString();
                } else {
                    this.statusCode = CampaignStatusCode.APPROVED.name();
                }
                break;
            }
            case CANCEL_APPRV: {
                this.statusCode = CampaignStatusCode.CANCEL_APPRV.toString();
                break;
            }
            default:
                break;
        }
    }
}
