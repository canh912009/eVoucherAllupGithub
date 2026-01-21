package com.evoucher.partner.service.exception.define_exception;

import com.evoucher.partner.service.common.Common;
import lombok.extern.slf4j.Slf4j;

/**
*  error code = 20 + vnpt error code
* **/
@Slf4j
public class VnptException extends CustomCodeException{
    public VnptException(int code, String message) {
        super(message);
        String fillZeroIn = "20".concat(String.format("%03d", code));
        int formattedCode = Integer.parseInt(fillZeroIn);
        super.setCode(formattedCode);
    }

    public static VnptException vnptUnknownException(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return new VnptException(Common.UNKNOWN_ERROR_CODE, throwable.getMessage());
    }
}
