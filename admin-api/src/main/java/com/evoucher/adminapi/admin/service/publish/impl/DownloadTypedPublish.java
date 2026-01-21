package com.evoucher.adminapi.admin.service.publish.impl;

import com.evoucher.adminapi.admin.dao.models.EVoucher;
import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.service.PublishServiceImpl;
import com.evoucher.adminapi.admin.service.models.PublishDTO;
import com.evoucher.adminapi.admin.service.models.PublishRequest;
import com.evoucher.adminapi.cms.service.models.DownloadedVouchersDTO;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Qualifier("downloadTypedPublish")
public class DownloadTypedPublish extends DeliveryAbstractService {


    public DownloadTypedPublish(DeliveryBridgeService bridgeService) {
        super(bridgeService);
    }


    @Override
    public void setTypeSpecificMetaData(PublishDTO publishDTO) {
        // Set download vouchers
        Optional<List<EVoucher>> vouchersOptional = bridgeService.eVoucherRepository.findByPublishId(publishDTO.getId());
        if (vouchersOptional.isPresent()) {
            List<DownloadedVouchersDTO> voucherDTOs = vouchersOptional.get().stream()
                    .filter(eVoucher -> eVoucher.getParentVoucherEv() == null && eVoucher.getOriginalEv() == null)
                    .map(eVoucher -> DownloadedVouchersDTO.builder()
                    .ev(eVoucher.getEV())
                    .price(eVoucher.getVoucherPrice())
                    .extPin(eVoucher.getExternalPinNo())
                    .expirationDate(eVoucher.getExpirationDate())
                    .shortLink(eVoucher.getShortLink())
                    .serialNo(eVoucher.getSerialNo())
                    .OTP(eVoucher.getParentVoucherToken())
                    .activationDate(eVoucher.getActivationDate())
                    .build()).collect(Collectors.toList());

            publishDTO.setDownloadVouchers(voucherDTOs);
        }
    }

    @Override
    public SMSType getSMSType() {
        return SMSType.DOWNLOAD;
    }

    @Override
    public void validatePublishRequest(PublishRequest publishRequest) throws CustomCodeException {
        PublishServiceImpl.validateNumberOfVoucher(publishRequest.getNumberOfVouchers());
    }
    @Override
    public void setUserInfoToPublishRequest(PublishRequest publishRequest) {
        publishRequest.setEndUsers(bridgeService.publishService.generateDownloadTypedEndUsers(publishRequest));
    }

    @Override
    public boolean isMetaDataPublishing() {
        return true;
    }
}
