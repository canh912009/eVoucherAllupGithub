package com.evoucher.adminapi.good.service.typed_service;

import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LimitedCountService implements UsingVoucherAbstract {
    @Override
    public void validateGoodByProductType(GoodsRequest request) throws CustomCodeException {
        // ensure limited count type has usage count value
        if (request.getUsageCount() == null) throw new CustomCodeException("Usage count is null", HttpStatus.BAD_REQUEST);
    }
}
