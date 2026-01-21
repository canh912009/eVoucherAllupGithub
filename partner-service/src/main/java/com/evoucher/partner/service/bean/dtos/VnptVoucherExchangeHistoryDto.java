package com.evoucher.partner.service.bean.dtos;

import com.evoucher.partner.service.bean.enum_type.ProcessResult;
import com.evoucher.partner.service.bean.enum_type.VnptExchangeType;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class VnptVoucherExchangeHistoryDto {
    private int id;
    private String ev;
    private VnptExchangeType exchangeType;
    private Long requestHistoryId;
    private String vnptRequestId;
    private String cardSerial;
    private String cardPin;
    private String providerCode;
    private String targetPhone;
    private Long faceValue;
    private Date expireDate;
    private ProcessResult result;
    private Date exchangeDate;

    public void ok() {
        this.setResult(ProcessResult.SUCCESS);
    }

    public void fail() {
        this.setResult(ProcessResult.FAILED);
    }
}
