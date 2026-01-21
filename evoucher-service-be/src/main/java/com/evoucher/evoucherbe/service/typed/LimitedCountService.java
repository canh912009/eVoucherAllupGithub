package com.evoucher.evoucherbe.service.typed;

import com.evoucher.evoucherbe.dto.VoucherDto;
import com.evoucher.evoucherbe.entity.EVoucher;
import com.evoucher.evoucherbe.exception.BalanceNotEnoughException;
import com.evoucher.evoucherbe.exception.OverInitBalanceException;
import com.evoucher.evoucherbe.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("limitedCountService")
@Slf4j
@RequiredArgsConstructor
public class LimitedCountService implements PayingService {
    @Override
    public void validateBeforePay(VoucherDto voucher, Double exchangeAmount) throws BalanceNotEnoughException {
        int usageTime = exchangeAmount.intValue();
        if (voucher.getUsageRemainingCount() < usageTime) {
            String errorMsg = String.format("voucher %s has balance: %d smaller than exchange amount %d",
                    voucher.getEV(), voucher.getUsageRemainingCount(), usageTime);
            log.error(errorMsg);
            throw new BalanceNotEnoughException(
                    errorMsg
            );
        }
    }

    @Override
    public void validateBeforePayBack(VoucherDto voucher, Double exchangeAmount) throws OverInitBalanceException {
        log.info("validate payback {} to voucher {}", exchangeAmount, voucher.getEV());
        int usageTime = exchangeAmount.intValue();
        if (voucher.getUsageRemainingCount() < usageTime) {
            String errorMsg = String.format("voucher %s has balance: %d smaller than exchange amount %d",
                    voucher.getEV(), voucher.getUsageRemainingCount(), usageTime);
            log.error(errorMsg);
            throw new BalanceNotEnoughException(
                    errorMsg
            );
        }
    }

    @Override
    public void doPaying(EVoucher voucher, Double exchangeAmount) throws BalanceNotEnoughException {
        log.info("minute {} voucher {} usage time from {}", exchangeAmount,  voucher.getEV(), voucher.getUsageRemainingCount());
        int usageTime = exchangeAmount.intValue();
        if (voucher.getUsageRemainingCount() < usageTime) {
            String errorMsg = String.format("voucher %s has balance: %d smaller than exchange amount %d",
                    voucher.getEV(), voucher.getUsageRemainingCount(), usageTime);
            log.error(errorMsg);
            throw new BalanceNotEnoughException(
                    errorMsg
            );
        }
        voucher.setUsageRemainingCount(voucher.getUsageRemainingCount() - usageTime);
    }

    @Override
    public void doPayingBack(EVoucher voucher, Double exchangeAmount) throws OverInitBalanceException {
        log.info("plus {} voucher {} usage time from {}", exchangeAmount,  voucher.getEV(), voucher.getUsageRemainingCount());
        int usageTime = exchangeAmount.intValue();
        if (voucher.getUsageRemainingCount() + usageTime > voucher.getUsageCount()) {
            String errorMsg = String.format("voucher %s has balance after payback: %d over init balance %d",
                    voucher.getEV(), voucher.getUsageRemainingCount() + usageTime, voucher.getUsageCount());
            log.error(errorMsg);
            throw new BalanceNotEnoughException(
                    errorMsg
            );
        }
        voucher.setUsageRemainingCount(voucher.getUsageRemainingCount() + usageTime);
    }

    @Override
    public boolean isUsable(EVoucher voucher) {
        return voucher.getUsageRemainingCount() > 0;
    }

    @Override
    public boolean isBackToNormalAfterCancel(EVoucher voucher) {
        return Objects.equals(voucher.getUsageRemainingCount(), voucher.getUsageCount());
    }
}
