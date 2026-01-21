package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.cms.service.models.ExternalPinUploadDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.ExternalPinUploadRequest;
import org.springframework.data.domain.Page;

public interface ExternalPinUploadService {

    ExternalPinUploadDTO findExternalPinUploadById(Integer id);

    ExternalPinUploadDTO createExternalPinUpload(ExternalPinUploadRequest externalPinUploadRequest);

    Page<ExternalPinUploadDTO> searchExternalPinUpload(FilterSearchCms filterSearchCms);
}
