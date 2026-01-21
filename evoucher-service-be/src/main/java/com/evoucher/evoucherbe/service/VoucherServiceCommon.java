package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.dto.VoucherGenerateInfo;
import com.evoucher.evoucherbe.entity.EVoucher;
import com.evoucher.evoucherbe.repository.EVoucherRepository;
import com.evoucher.evoucherbe.repository.SettlementLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherServiceCommon {

    private final EVoucherRepository eVoucherRepository;

    private final SettlementLogRepository settlementLogRepository;


    @Transactional
    public List<EVoucher> saveInformationOfVoucherChoice(VoucherGenerateInfo voucherGenerateInfo,
                                                         EVoucher voucherParent) {
        Date exchangeDate = new Date();
        // get list voucher
        List<EVoucher> voucherResult = voucherGenerateInfo.getEVouchers();

        // save voucher
        log.info("Save List EVoucher with publishId: {} and evParent: {}",
                voucherParent.getPublishId(), voucherParent.getEV());
        voucherResult = eVoucherRepository.saveAll(voucherResult);

        // save list settlement log
        log.info("Save list SettlementLog with publishId: {} and evParent: {}",
                voucherParent.getPublishId(), voucherParent.getEV());
        settlementLogRepository.saveAll(voucherGenerateInfo.getSettlementLogs());

        log.info("Update balance of Voucher Parent: {}", voucherParent.getEV());

        //update voucher last exchange date
        voucherParent.setLastExchangeDate(exchangeDate);
        eVoucherRepository.save(voucherParent);

        return voucherResult;
    }
}
