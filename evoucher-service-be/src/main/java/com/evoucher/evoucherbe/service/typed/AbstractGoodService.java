package com.evoucher.evoucherbe.service.typed;

import com.evoucher.evoucherbe.common.enums.PeriodType;
import com.evoucher.evoucherbe.dto.EVoucherHistoryProcess;
import com.evoucher.evoucherbe.dto.GoodDto;
import com.evoucher.evoucherbe.dto.VoucherDto;
import com.evoucher.evoucherbe.dto.VoucherExchangeReq;
import com.evoucher.evoucherbe.entity.EVoucher;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.message.BaseResponse;

import java.util.Date;

public interface AbstractGoodService {
    BaseResponse processUsingVoucher(VoucherExchangeReq request, VoucherDto voucher, GoodDto good) throws CustomCodeException;
    void validateGoodExpiredDate(Long id, PeriodType type, Integer periodTerm, String goodExpireDate);
}
