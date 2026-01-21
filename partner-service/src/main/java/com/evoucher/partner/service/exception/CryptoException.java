package com.evoucher.partner.service.exception;

import com.evoucher.partner.service.common.Common;
import com.evoucher.partner.service.exception.define_exception.CustomCodeException;

public class CryptoException extends CustomCodeException {
    public CryptoException(String message) {
        super(Common.SERVER_ERROR, message);
    }
}
