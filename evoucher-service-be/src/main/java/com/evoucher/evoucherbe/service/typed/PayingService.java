package com.evoucher.evoucherbe.service.typed;

import com.evoucher.evoucherbe.dto.VoucherDto;
import com.evoucher.evoucherbe.entity.EVoucher;
import com.evoucher.evoucherbe.exception.BalanceNotEnoughException;
import com.evoucher.evoucherbe.exception.OverInitBalanceException;

public interface PayingService {
    void validateBeforePay(VoucherDto voucher, Double exchangeAmount) throws BalanceNotEnoughException;
    void validateBeforePayBack(VoucherDto voucher, Double exchangeAmount) throws OverInitBalanceException;
    void doPaying(EVoucher voucher, Double exchangeAmount) throws BalanceNotEnoughException;
    void doPayingBack(EVoucher voucher, Double exchangeAmount) throws OverInitBalanceException;
    boolean isUsable(EVoucher voucher);
    boolean isBackToNormalAfterCancel(EVoucher voucher);
}
