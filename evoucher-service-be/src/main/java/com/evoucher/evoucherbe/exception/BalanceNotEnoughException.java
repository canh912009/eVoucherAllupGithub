package com.evoucher.evoucherbe.exception;

public class BalanceNotEnoughException extends ClientException {
    public BalanceNotEnoughException(String msg) {
        super(msg);
    }
}
