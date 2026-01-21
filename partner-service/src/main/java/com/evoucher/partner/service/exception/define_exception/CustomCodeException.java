package com.evoucher.partner.service.exception.define_exception;

import com.evoucher.partner.service.common.Common;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustomCodeException extends RuntimeException implements Serializable {
    private int code;

    public CustomCodeException(int code, String message) {
        super(message);
        this.code = code;
    }

    protected CustomCodeException(String message) {
        super(message);
    }
    protected void setCode(int code) {
        this.code = code;
    }

    public static CustomCodeException serverError(Throwable throwable) {
        return new CustomCodeException(Common.SERVER_ERROR, throwable.getMessage());
    }
}
