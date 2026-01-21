package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.VoucherExchangeHistoryRepository;
import com.evoucher.adminapi.admin.service.models.ChildOfChoiceVoucherResponse;
import com.evoucher.adminapi.admin.service.models.CsExchangeHistoryDTO;
import com.evoucher.adminapi.admin.service.models.PinDetailResponse;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoucherExchangeHistoryService {
    private final VoucherExchangeHistoryRepository repository;

    public List<CsExchangeHistoryDTO> findExchangeHistoryByVoucherId(String voucherId) throws CustomCodeException {
        try {
            log.info("find exchange histories by voucher id: {}", voucherId);
            return repository.findExchangeHistoryByVoucherId(voucherId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public PinDetailResponse findPinDetailById(String voucherId) throws CustomCodeException {
        try {
            log.info("find pin detail by voucher id: {}", voucherId);
            return repository.findPinDetailById(voucherId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ChildOfChoiceVoucherResponse> findChildOfChoiceVoucherByChoiceVoucherId(String voucherId)
            throws CustomCodeException {
        try {
            log.info("Find Child of Choice voucher by choice voucher id: {}", voucherId);
            return repository.findChildOfChoiceVoucherByChoiceVoucherId(voucherId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
