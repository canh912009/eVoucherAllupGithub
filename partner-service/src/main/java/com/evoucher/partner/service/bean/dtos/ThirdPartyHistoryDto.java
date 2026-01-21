package com.evoucher.partner.service.bean.dtos;

import com.evoucher.partner.service.bean.enum_type.ProcessResult;
import com.evoucher.partner.service.bean.enum_type.ThirdRequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyHistoryDto {
    private Long id;
    private String system;
    private String requestUrl;
    private Date requestTime;
    private String requestBody;
    private Date responseTime;
    private String responseBody;
    private String result;
    private String description;
    private Long uploadId;
    private ThirdRequestType requestType;

    public void receiveResponse(String responseBody) {
        this.responseBody = responseBody;
        this.responseTime = new Date();
    }

    public void sendRequest(String requestBody) {
        this.requestBody = requestBody;
        this.requestTime = new Date();
    }

    public void ok() {
        this.setResult(ProcessResult.SUCCESS.name());
    }

    public void fail(String description) {
        this.setResult(ProcessResult.FAILED.name());
        this.description = description;
    }

}
