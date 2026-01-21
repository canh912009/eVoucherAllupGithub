package com.evoucher.evoucherbe.service.typed.system;

import com.evoucher.evoucherbe.common.enums.PeriodType;
import com.evoucher.evoucherbe.dto.GoodDto;
import com.evoucher.evoucherbe.dto.VoucherDto;
import com.evoucher.evoucherbe.dto.VoucherExchangeReq;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.message.BaseResponse;
import com.evoucher.evoucherbe.service.GoodBasicService;
import com.evoucher.evoucherbe.service.typed.AbstractGoodService;
import com.evoucher.evoucherbe.utils.ErrorCode;
import com.evoucher.evoucherbe.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public abstract class SystemAbstractService implements AbstractGoodService {
    protected final SystemBridgeService bridgeService;
    @Override
    public BaseResponse processUsingVoucher(VoucherExchangeReq request, VoucherDto voucher, GoodDto good) throws CustomCodeException {
        log.info("{} type no need to process more", good.getSystem());
        return new BaseResponse();
    }
    @Override
    public void validateGoodExpiredDate(Long id, PeriodType type, Integer periodTerm, String goodExpireDate) {
        Date expiredDate = bridgeService.getVoucherService().getGoodEndDate(type, periodTerm, goodExpireDate);
        if (new Date().after(expiredDate)) {
            log.error("good {} is expired at {}", id, expiredDate);
            throw new CustomCodeException(
                    MessageUtils.getMessage(GoodBasicService.ErrorString.EXPIRE_GOOD),
                    GoodBasicService.ErrorCode.EXPIRED_GOOD,
                    HttpStatus.BAD_REQUEST);
        }
    }
}
