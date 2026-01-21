package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.service.models.ExternalPinUploadDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.PinSearchDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExternalPinUploadRepositoryCustom {
    List<ExternalPinUploadDTO> searchExternalPinUpload(FilterSearchCms filterSearchCms, Pageable pageable);

    Long countExternalPinUpload(FilterSearchCms filterSearchCms, Pageable pageable);
    List<PinSearchDTO> searchVnptPin(String brandId, String brandTitle, Pageable pageable);
    Long countVnptPin(String brandId, String brandTitle);
}
