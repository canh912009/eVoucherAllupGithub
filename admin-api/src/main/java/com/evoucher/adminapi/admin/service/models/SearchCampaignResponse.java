package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.admin.enums.CampaignStatusCode;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.models.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCampaignResponse extends BaseDTO {
    private Integer id;
    private String campaignName;
    private String customerId;
    private String customerName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private String validYn;
    private String approveStatusCode;
    private String statusCode;
    private String customerType;

    public void setStatusCode(SearchCampaignResponse searchCampaignResponse) {
        if (Objects.isNull(searchCampaignResponse.getApproveStatusCode())
                || Objects.isNull(searchCampaignResponse.getStartDate())
                || Objects.isNull(searchCampaignResponse.getEndDate())) return;

        ApproveStatus approveStatusCode = ApproveStatus.valueOf(searchCampaignResponse.getApproveStatusCode());
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
