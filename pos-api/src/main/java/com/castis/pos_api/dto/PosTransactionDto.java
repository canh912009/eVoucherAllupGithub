package com.castis.pos_api.dto;

import com.castis.pos_api.enum_constant.PosRequestType;
import com.castis.pos_api.enum_constant.ProcessResult;
import lombok.Data;

import java.util.Date;

@Data
public class PosTransactionDto {
    private String transactionId;
    private Integer keyType;
    private String key;
    private String password;
    private String userPhoneNo;
    private String posCd;
    private String brandId;
    private String storeId;
    private String ev;
    private Date requestDate;
    private Date responseDate;
    private ProcessResult result;
    private PosRequestType requestType;
    private Double prepaidAmount;
    private String requestBody;
    private String responseBody;
    private String appId;

    public void error(String cause) {
        this.responseDate = new Date();
        this.result = ProcessResult.FAILED;
        this.responseBody = cause;
    }

    public void success(String response) {
        this.responseBody = response;
        this.responseDate = new Date();
        this.result = ProcessResult.SUCCESS;
    }
}
