package com.evoucher.evoucherbe.service.typed;

import com.evoucher.evoucherbe.common.enums.VoucherTypeCode;
import com.evoucher.evoucherbe.dto.VoucherDto;
import com.evoucher.evoucherbe.entity.EVoucher;
import com.evoucher.evoucherbe.exception.BalanceNotEnoughException;
import com.evoucher.evoucherbe.exception.OverInitBalanceException;
import com.evoucher.evoucherbe.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("prepaidService")
@Slf4j
@RequiredArgsConstructor
public class BalancePayingService implements PayingService {
    @Override
    public void validateBeforePay(VoucherDto voucher, Double exchangeAmount) throws BalanceNotEnoughException {
        log.info("valid paying {} for voucher {}", exchangeAmount, voucher.getEV());
        if (voucher.getVoucherTypeCode() == VoucherTypeCode.PP) {
            double doubleExchangeAmount = exchangeAmount;
            if (voucher.getBalance() < doubleExchangeAmount) {
                String errorMsg = String.format("voucher %s has balance: %f smaller than exchange amount %f", voucher.getEV(), voucher.getBalance(), doubleExchangeAmount);
                log.error(errorMsg);
                throw new BalanceNotEnoughException(
                        errorMsg
                );
            }
        }
    }

    @Override
    public void validateBeforePayBack(VoucherDto voucher, Double exchangeAmount) throws OverInitBalanceException {
        log.info("validate payback {} for voucher : {}", exchangeAmount, voucher.getEV());
        if (voucher.getBalance() + exchangeAmount > voucher.getInitAmount()) {
            String errorMsg = String.format("voucher %s has balance after pay back: %f over init balance %f", voucher.getEV(), voucher.getBalance() + exchangeAmount, voucher.getInitAmount());
            log.error(errorMsg);
            throw new BalanceNotEnoughException(
                    errorMsg
            );
        }
    }

    @Override
    public void doPayingBack(EVoucher voucher, Double exchangeAmount) throws BalanceNotEnoughException {
        // update balance of parent voucher
        if (voucher.getBalance() + exchangeAmount > voucher.getInitAmount()) {
            String errorMsg = String.format("voucher %s has balance after pay back: %f over init balance %f", voucher.getEV(), voucher.getBalance() + exchangeAmount, voucher.getInitAmount());
            log.error(errorMsg);
            throw new BalanceNotEnoughException(
                    errorMsg
            );
        }
        double newBalance = DataUtils.roundingNumber(voucher.getBalance() + (double) exchangeAmount);
        voucher.setBalance(newBalance);
        log.info("Voucher {} balance restored from {} to {}", voucher.getEV(), voucher.getBalance(), newBalance);
    }

    @Override
    public void doPaying(EVoucher voucher, Double exchangeAmount) throws OverInitBalanceException {
        // update balance of parent voucher
        log.info("minus {} voucher {} balance from {}", exchangeAmount,  voucher.getEV(), voucher.getBalance());
        if (voucher.getBalance() < exchangeAmount) {
            String errorMsg = String.format("voucher %s has balance: %f smaller than exchange amount %f", voucher.getEV(), voucher.getBalance(), exchangeAmount);
            log.error(errorMsg);
            throw new BalanceNotEnoughException(
                    errorMsg
            );
        }
        voucher.setBalance(DataUtils.roundingNumber(voucher.getBalance() - (double) exchangeAmount));
    }

    @Override
    public boolean isUsable(EVoucher voucher) {
        return voucher.getBalance() > 0;
    }

    @Override
    public boolean isBackToNormalAfterCancel(EVoucher voucher) {
        return Objects.equals(voucher.getBalance(), voucher.getInitAmount());
    }
}
