package com.evoucher.evoucherbe.exception;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChoiceVoucherProcessException extends RuntimeException implements Serializable {
    private int status;

    public ChoiceVoucherProcessException(String message, int status) {
        super(message);
        this.status = status;
    }
}
